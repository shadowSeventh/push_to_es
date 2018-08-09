package com.shadow.push;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class PushToEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushToEsApplication.class, args);
    }
}


