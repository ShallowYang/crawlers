package com.baza.crawler.service;

import com.baza.crawler.dao.LinkedinBaiDuSearchMapper;
import com.baza.crawler.domain.LinkedinBaiDuSearch;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/16
 * Time: 11:47
 * Description:
 */
@Component
public class BaiduMain {

    @Autowired
    private LinkedinBaiDuSearchMapper mapper;

    final static int pageSize = 10;

    static String search = "";

    public void crawler() throws IOException {

        String datakeys = "li,wang,zhang,liu,chen,yang,zhao,huang,zhou,wu,xu,sun,hu,zhu,gao,lin,he,guo,ma,luo,liang,song,zheng,xie,han,tang,feng,yu,dong,xiao,cheng,cao,yuan,deng,xu,fu,shen,zeng,peng,lv,su,lu,jiang,cai,jia,ding,wei,xue,ye,yan,yu,pan,du,dai,xia,zhong,wang,tian,ren,jiang,fan,fang,shi,yao,tan,liao,zhou,xiong,jin,lu,hao,kong,bai,cui,kang,mao,qiu,qin,jiang,shi,gu,hou,shao,meng,long,wan,duan,lei,qian,tang,yin,li,yi,chang,wu,qiao,he,lai,gong,wen,pang,fan,lan,yin,shi,tao,hong,zhai,an,yan,ni,yan,niu,wen,lu,ji,yu,zhang,lu,ge,wu,wei,shen,you,bi,nie,cong,jiao,xiang,liu,xing,lu,yue,qi,yan,mei,mo,zhuang,xin,guan,zhu,zuo,tu,gu,qi,shi,shu,geng,mou,bo,lu,zhan,guan,miao,ling,fei,ji,le,sheng,tong,ou,zhen,xiang,qu,cheng,you,yang,pei,xi,wei,cha,qu,bao,wei,tan,huo,weng,sui,zhi,gan,jing,bo,dan,bao,si,bai,ning,ke,ruan,gui,min,ouyang,jie,qiang,chai,hua,che,ran,fang,bian";
//        String datakeys = "wang,";
        List<String> strings = Arrays.asList(datakeys.split(","));
        for(String item:strings){

            search = item;
            try{
                searchResult(item,item + " site:cn.linkedin.com", 1);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }

        }

    }

//    public static void main(String[] args) throws IOException {
//        searchResult("龙璐 site:cn.linkedin.com", 1);
//    }

    public void searchResult(String search,String keyword, int page) throws IOException {
        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);

        while (true) {
            // 限制，如果数据采集错误，或者无限的时候，就限制100页
            if(page==100){
                break;
            }
            String url = "http://www.baidu.com/s?pn=" + (page - 1) * pageSize + "&wd=" + keyword;
            HtmlPage result = client.getPage(url);
            System.out.println(result.getUrl());
            String contentAsString = result.getWebResponse().getContentAsString();
//            System.out.println(contentAsString);
            try {
                parseContent(contentAsString);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            page++;
        }
    }

    public void parseContent(String content) throws Exception {
        LinkedinBaiDuSearch linkedinBaiDuSearch = new LinkedinBaiDuSearch();
        Document parse = Jsoup.parse(content);
        Elements select = parse.select("div[tpl]");
        for(Element ele:select) {
            try {
                String text = ele.select("div.c-abstract").get(0).text();
                String summary = "";
                if(text.contains("(")){
                    int start = text.indexOf("(");
                    int end = text.lastIndexOf(")");
                    if(end>0){
                        summary = text.substring(start + 1, end);
                    }else{
                        summary = text.substring(start + 1, text.length());
                    }

                }

                System.out.println(summary);
                linkedinBaiDuSearch.setSummary(summary);
                String tools = ele.select("div.c-tools").get(0).attr("data-tools");
                String title = JsonPath.parse(tools).read("$.title").toString();
                System.out.println(title);
                linkedinBaiDuSearch.setName(title.substring(0, title.lastIndexOf("|")));
                linkedinBaiDuSearch.setUrl(JsonPath.parse(tools).read("$.url").toString());
                linkedinBaiDuSearch.setCrawled(LinkedinBaiDuSearch.NO);
                linkedinBaiDuSearch.setSearch(search);
                mapper.insert(linkedinBaiDuSearch);
//                System.out.println(linkedinBaiDuSearch);
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
        Elements select1 = parse.select("div#page a");
        Element last = select1.last();

        // 如果没有下一页，就结束爬取
        if (StringUtils.isEmpty(last.className())) {
            throw new Exception("no reslt");
        }
    }
}
