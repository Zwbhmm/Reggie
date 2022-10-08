package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//lombok提供的注解，用来生成日志
@Slf4j
@SpringBootApplication
@ServletComponentScan
public class ReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
//        由注解@Slf4j生成
        log.info("项目启动成功");
    }

}
