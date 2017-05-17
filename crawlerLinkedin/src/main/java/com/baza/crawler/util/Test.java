package com.baza.crawler.util;

import com.baza.crawler.domain.LinkedinModel;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.sf.json.JSONObject;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/17
 * Time: 14:22
 * Description:
 */
public class Test {

    final static String LOGIN_URL = "https://www.linkedin.com/uas/login"; // 登录地址

    public static void main(String[] args) {
        try{
            WebClient webClient = new WebClient(BrowserVersion.CHROME);// 创建WebClient
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.setJavaScriptTimeout(100000);//设置JS执行的超时时间
            webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
            webClient.getOptions().setCssEnabled(false);
            HtmlPage page = webClient.getPage(LOGIN_URL); // 打开linkedin
            HtmlElement usernameEle = page.getElementByName("session_key");
            HtmlElement passwordEle = (HtmlElement) page.getElementById("session_password-login");
            usernameEle.focus(); // 设置输入焦点
            usernameEle.type("772704457@qq.com");
            passwordEle.focus(); // 设置输入焦点
            passwordEle.type("wxc772704457@qq");
            HtmlElement submitEle = page.getElementByName("signin");
            page =  submitEle.click();
            Thread.sleep(5000);

            Page page1 = webClient.getPage("https://cn.linkedin.com/topic/lifor?trk=pprofile_topic");

            System.out.println(page1.getUrl());
            LinkedinModel linkedinModel = LinkedInUtil.parseInPage(page1.getWebResponse().getContentAsString());
            System.out.println(JSONObject.fromObject(linkedinModel));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
