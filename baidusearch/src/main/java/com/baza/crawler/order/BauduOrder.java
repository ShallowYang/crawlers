package com.baza.crawler.order;

import com.baza.crawler.service.BaiduMain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/16
 * Time: 14:10
 * Description:
 */
@Component
@Order
public class BauduOrder implements CommandLineRunner {

    @Resource
    private BaiduMain baiduMain;

    @Override
    public void run(String... strings) throws Exception {
        baiduMain.crawler();
    }
}
