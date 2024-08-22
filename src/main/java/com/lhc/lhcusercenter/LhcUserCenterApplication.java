package com.lhc.lhcusercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.lhc.lhcusercenter.db.mapper")
public class LhcUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LhcUserCenterApplication.class, args);
    }

}
