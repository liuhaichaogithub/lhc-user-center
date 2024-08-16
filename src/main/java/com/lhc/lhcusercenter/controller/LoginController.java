package com.lhc.lhcusercenter.controller;

import com.lhc.lhcusercenter.common.pojo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Author: 刘海潮
 * Date: 2024/8/16 下午4:25
 */
@RestController
@Slf4j
@RequestMapping("/login")
@Validated
public class LoginController {
    @GetMapping("/getImageCode")
    public Response<?> pageVc() {
        log.info("请求到了接口");
        return Response.success();
    }
}
