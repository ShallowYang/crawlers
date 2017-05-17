package com.baza.crawler.service;

import com.baza.crawler.dao.LinkedinBaiDuSearchMapper;
import com.baza.crawler.dao.LinkedinModelMapper;
import com.baza.crawler.domain.LinkedinBaiDuSearch;
import com.baza.crawler.domain.LinkedinModel;
import com.baza.crawler.util.LinkedInUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/17
 * Time: 13:54
 * Description:
 */
@Component
public class LinkedinService {

    Logger logger = Logger.getLogger(LinkedinService.class);

    final static String LOGIN_URL = "https://www.linkedin.com/uas/login"; // 登录地址
    final static Pattern r = Pattern.compile("in/[\\u4e00-\\u9fa5-0-9-a-z-A-z]*");
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private LinkedinBaiDuSearchMapper baiDuSearchMapper;

    @Autowired
    private LinkedinModelMapper linkedinModelMapper;

    public static void main(String[] args) throws InterruptedException {
        LinkedinService ls = new LinkedinService();

        ls.run();
    }

    public void run() throws InterruptedException {
        while (true){
            try {

                List<LinkedinBaiDuSearch> linkedinBaiDuSearches = baiDuSearchMapper.selectByNoCrawler();
                logger.info("待抓取数据为:"+linkedinBaiDuSearches.size());

                // 登录开始
                final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);// 创建WebClient
                webClient.getOptions().setUseInsecureSSL(true);
                webClient.getOptions().setJavaScriptEnabled(false);
                webClient.getOptions().setCssEnabled(false);
//                webClient.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
                webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
                HtmlPage page = webClient.getPage(LOGIN_URL); // 打开linkedin
                HtmlElement usernameEle = page.getElementByName("session_key");
                HtmlElement passwordEle = (HtmlElement) page.getElementById("session_password-login");
                usernameEle.focus(); // 设置输入焦点
                usernameEle.type("wdc43101289217@hotmail.com");
                passwordEle.focus(); // 设置输入焦点
                passwordEle.type("Its4bz");
                HtmlElement submitEle = page.getElementByName("signin");
                Page click = submitEle.click();
                // 登录结束



                for(LinkedinBaiDuSearch every:linkedinBaiDuSearches){
                    try {
                        System.out.println(every.getUrl());
                        HtmlPage targetPage = webClient.getPage(every.getUrl());

                        // 考虑到国内访问国外服务器的速度原因，故每个等待2s   后期待优化
                        String targetUrl = targetPage.getUrl().toString();

                        if(targetUrl.contains("/topic/")){
                            every.setCrawled(LinkedinBaiDuSearch.OTHER);
                            baiDuSearchMapper.updateByPrimaryKey(every);
                            continue;
                        }

                        LinkedinModel linkedInModel = LinkedInUtil.parseInPage(targetPage.getWebResponse().getContentAsString());
                        Matcher m = r.matcher(targetUrl);
                        String inUn = "";
                        if (m.find()) {
                            inUn = m.group();
                            inUn = inUn.substring(3, inUn.length());
                        } else {
                            System.out.println(targetUrl);
                            inUn = targetUrl.split("http://www.linkedin.com/in/")[1];
                        }
                        linkedInModel.setUniqueUrl(inUn);
                        linkedInModel.setInsertTime(new Date());
                        System.out.println(JSONObject.fromObject(linkedInModel));
//                        linkedinModelMapper.insert(linkedInModel);
//                        every.setCrawled(LinkedinBaiDuSearch.YES);
//                        baiDuSearchMapper.updateByPrimaryKey(every);
                    } catch (Exception e) {
                        logger.error(e.getCause());
//                        Thread.sleep(2000);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
//                Thread.sleep(2000);
                logger.error(e.getCause());
                e.printStackTrace();
            }

            logger.info("等待十分钟，再继续抓取");
            Thread.sleep(10*60*1000);
        }
    }
}
