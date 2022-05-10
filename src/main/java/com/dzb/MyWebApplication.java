package com.dzb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author : zhengbo.du
 * @date : 2022/2/4 17:51
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableWebMvc
@EnableScheduling
public class MyWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWebApplication.class,args);
    }
}
