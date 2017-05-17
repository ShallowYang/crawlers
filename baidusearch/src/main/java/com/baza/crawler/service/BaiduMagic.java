package com.baza.crawler.service;//package crawler;
//
//import com.baza.crawler.domain.LinkedinBaiDuSearch;
//import com.jayway.jsonpath.DocumentContext;
//import com.jayway.jsonpath.JsonPath;
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.pipeline.ConsolePipeline;
//import us.codecraft.webmagic.processor.PageProcessor;
//import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
//import us.codecraft.webmagic.scheduler.QueueScheduler;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created with IDEA
// * User: vector
// * Data: 2017/5/15
// * Time: 19:07
// * Description:
// */
//public class BaiduMagic implements PageProcessor {
//
//    public void process(Page page) {
//
//        page.addTargetRequests(page.getHtml().xpath("//div[@id='page']//a/@href").all());
////        List<String> all = ;//.forEach(link-> System.out.println(link.toString()));
////        all.forEach(link-> System.out.println(link));
////        page.putField("name",);
//        page.getTargetRequests().forEach(target-> System.out.println(target));
////        System.out.println("目标大小：" + targetRequests.size());
//        List<String> all = page.getHtml().xpath("//div[@class='result c-container']//div[@class='f13']//div[@class='c-tools']/@data-tools").all();
//        all.forEach(tool-> {
//            DocumentContext parse = JsonPath.parse(tool);
//            String titleTmp = parse.read("$.title").toString();
//            LinkedinBaiDuSearch linkedinBaiDuSearch = new LinkedinBaiDuSearch();
//            linkedinBaiDuSearch.setInsertTime(new Date());
//            linkedinBaiDuSearch.setName(titleTmp.substring(0,titleTmp.lastIndexOf("|")));
//            linkedinBaiDuSearch.setCrawled(LinkedinBaiDuSearch.NO);
//            linkedinBaiDuSearch.setUrl(parse.read("$.url"));
////            System.out.println(linkedinBaiDuSearch.toString());
//        });
//
//    }
//
//    public Site getSite() {
//        return Site.me().setRetryTimes(3).setSleepTime(10).setSleepTime(1000);
//    }
//
//    public static void main(String[] args) {
//        Spider.create(new BaiduMagic())
//                // 从"http://www.imooc.com/user/setprofile"开始抓
//                .addUrl("http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1" +
//                        "&tn=baidu&wd=wang%20site%3Acn.linkedin.com" +
//                        "&oq=wang%2520site%253Acn.linkedin.com" +
//                        "&rsv_pq=cf8343c20011b9e3&rsv_t=0145AElB1Xlp5hMqct8HQWjgVt7WI14eXFHrYogDFTHmxXqo9LIPipZQsxQ&rqlang=cn&rsv_enter=0")
//                .addPipeline(new ConsolePipeline())
//                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000)))
//
//                // 开启5个线程抓取
//                .thread(2)
//                // 启动爬虫
//                .run();
//    }
//
//}
