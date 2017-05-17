package com.baza.crawler.service;

import com.baza.crawler.dao.LinkedinBaiDuSearchMapper;
import com.baza.crawler.domain.LinkedinBaiDuSearch;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jayway.jsonpath.JsonPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/15
 * Time: 17:04
 * Description:
 */
@Component
public class BaiDuSearch {

    @Autowired
    private LinkedinBaiDuSearchMapper mapper;


//    final static  int pageSize = 20;
    ExecutorService executorService = Executors.newCachedThreadPool();
    private static HashSet<String> pageCache = new HashSet<>();
    private static Queue<String> targetLis= new LinkedList<String>();
    private final static String BAI_DU = "http://www.baidu.com";




    public void run() throws IOException {
        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setCssEnabled(false);
        //获取爬取链接
//        Document parse = Jsoup.parse(contentAsString);
        int flag = 1;
        while (true) {
            try {
                    if (flag == 1) {
                        HtmlPage page = client.getPage("https://www.baidu.com/");
                        HtmlElement kw = (HtmlElement) page.getElementById("kw");
                        HtmlElement searchBtn = (HtmlElement) page.getElementById("su");
                        kw.focus();
                        kw.type("马云 site:cn.linkedin.com");
                        HtmlPage result = searchBtn.click();
                        String contentAsString = result.getWebResponse().getContentAsString();
                        parseTargetUrl(contentAsString);
                        executorService.execute(()->parseContent(contentAsString));
                        flag++;
                    } else {
                        Thread.sleep(2000);
                        String targetUrl = targetLis.poll();
                        System.out.println(targetUrl);
                        HtmlPage result = client.getPage(targetUrl);
                        String contentAsString = result.getWebResponse().getContentAsString();
                        targetLis.remove(0);
                        parseTargetUrl(contentAsString);
                        executorService.execute(()->parseContent(contentAsString));
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void parseTargetUrl(String content) {

        Document parse = Jsoup.parse(content);
        Elements select = parse.select("div#page a");
        select.forEach(a -> {
            try {
                String href = a.attr("href");
                String pattern = "(pn=[0-9]*)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(href.toString());
                if (m.find()) {
                    String temp = m.group();
                    String substring = temp.substring(3, temp.length());
                    if (pageCache.add(substring)) {
                        targetLis.offer(BAI_DU+href);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(targetLis.size());
    }


    public void parseContent(String content) {
        LinkedinBaiDuSearch linkedinBaiDuSearch = new LinkedinBaiDuSearch();
        Document parse = Jsoup.parse(content);
        Elements select = parse.select("div[tpl]");
        select.forEach(ele -> {
            try {
                String text = ele.select("div.c-abstract").get(0).text();
                int start = text.indexOf("(");
                String summary = "";
                if (start > 0) {
                    int end = text.lastIndexOf(")");
                    summary = text.substring(start + 1, end);
                }
                linkedinBaiDuSearch.setSummary(summary);
                String tools = ele.select("div.c-tools").get(0).attr("data-tools");
                String title = JsonPath.parse(tools).read("$.title").toString();
                linkedinBaiDuSearch.setName(title.substring(0, title.lastIndexOf("|")));
                linkedinBaiDuSearch.setUrl(JsonPath.parse(tools).read("$.url").toString());
                linkedinBaiDuSearch.setCrawled(LinkedinBaiDuSearch.NO);
//                mapper.insert(linkedinBaiDuSearch);
                System.out.println(linkedinBaiDuSearch);
            } catch (Exception e) {

                e.printStackTrace();
            }

        });
    }
}
