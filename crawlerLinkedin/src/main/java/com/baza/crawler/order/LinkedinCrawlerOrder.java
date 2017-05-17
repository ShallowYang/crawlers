package com.baza.crawler.order;

import com.baza.crawler.service.LinkedinService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/17
 * Time: 14:55
 * Description:
 */
@Component
@Order
public class LinkedinCrawlerOrder implements CommandLineRunner {

    @Resource
    private LinkedinService service;

    @Override
    public void run(String... strings) throws Exception {
        service.run();
    }
}
