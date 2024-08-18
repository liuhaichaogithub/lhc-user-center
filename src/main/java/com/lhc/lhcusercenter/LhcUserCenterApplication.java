package com.lhc.lhcusercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class LhcUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LhcUserCenterApplication.class, args);
    }

}
