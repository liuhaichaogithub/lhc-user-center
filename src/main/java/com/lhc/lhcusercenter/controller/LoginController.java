package com.lhc.lhcusercenter.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.lhc.lhcusercenter.common.exception.enums.GlobalErrorCodeConstants;
import com.lhc.lhcusercenter.common.pojo.Response;
import com.lhc.lhcusercenter.entity.req.LoginReqDto;
import jakarta.annotation.Resource;
import jodd.util.StringUtil;
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
@RequestMapping("/user")
@Validated
public class LoginController {

    @Resource
    RedissonClient redissonClient;

    @GetMapping("/getImageCode")
    public Response<?> pageVc(@RequestHeader(name = "traceId") String traceId) {
        RMapCache<String, String> mapCache = redissonClient.getMapCache("user-center:imageCode");
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        final String code = lineCaptcha.getCode();
        final String imageBase64Data = lineCaptcha.getImageBase64Data();
        log.info("验证码是{} traceId is {}", code, traceId);
        mapCache.put(traceId, code, 5, TimeUnit.MINUTES);
        return Response.success(imageBase64Data);
    }

    @PostMapping("/login")
    public Response<?> login(@RequestBody @Validated LoginReqDto loginReqDto) {
        RMapCache<String, String> mapCache = redissonClient.getMapCache("user-center:imageCode");
        final String imageCode = mapCache.get(loginReqDto.getImageCodeTraceId());
        if (StringUtil.isBlank(imageCode)) {
            return Response.error(GlobalErrorCodeConstants.imageCodeError);
        }
        if(!imageCode.equalsIgnoreCase(loginReqDto.getImageCode())){
            return Response.error(GlobalErrorCodeConstants.imageCodeError);
        }

        return Response.success();
    }

    public static void main(String[] args) {
        final String gensalt = BCrypt.gensalt();
        System.out.println(gensalt);
        String pw_hash = BCrypt.hashpw("liu821290375", "$2a$10$EyUsJ2hvGDj5gmwFfeKnSe");
//        $2a$10$EyUsJ2hvGDj5gmwFfeKnSeOk/hIH5mxw6jbvAXBKh6GSI7zeuA4UK
        System.out.println(pw_hash);

    }
}
