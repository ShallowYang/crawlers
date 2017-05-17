package com.baza.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/17
 * Time: 14:57
 * Description:
 */
@SpringBootApplication
@ServletComponentScan
//@EnableTransactionManagement // 事务启动注解
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
