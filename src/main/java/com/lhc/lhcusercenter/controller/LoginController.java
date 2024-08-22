package com.lhc.lhcusercenter.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.lhc.lhcusercenter.common.pojo.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Author: 刘海潮
 * Date: 2024/8/16 下午4:25
 */
@RestController
@Slf4j
@RequestMapping("/login")
@Validated
public class LoginController {

    @Resource
    RedissonClient redissonClient;

    @GetMapping("/getImageCode")
    public Response<?> pageVc(@RequestHeader(name = "traceId") String traceId) {
        RMapCache<Object, Object> mapCache = redissonClient.getMapCache("user-center:imageCode");
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        final String code = lineCaptcha.getCode();
        final String imageBase64Data = lineCaptcha.getImageBase64Data();
        log.info("验证码是{} traceId is {}", code, traceId);
        mapCache.put(traceId, code, 5, TimeUnit.MINUTES);
        return Response.success(imageBase64Data);
    }
}
