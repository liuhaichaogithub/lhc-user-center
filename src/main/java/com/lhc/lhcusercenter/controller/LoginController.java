package com.lhc.lhcusercenter.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lhc.lhcusercenter.common.exception.enums.GlobalErrorCodeConstants;
import com.lhc.lhcusercenter.common.pojo.Response;
import com.lhc.lhcusercenter.db.UserInfo;
import com.lhc.lhcusercenter.db.service.UserInfoService;
import com.lhc.lhcusercenter.entity.req.LoginReqDto;
import com.lhc.lhcusercenter.entity.rsp.LoginRspDto;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
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
    @Resource
    UserInfoService userInfoService;

    @GetMapping("/getImageCode/{imageCodeTraceId}")
    public Response<String> pageVc(@PathVariable(name = "imageCodeTraceId") String imageCodeTraceId) {
        RMapCache<String, String> mapCache = redissonClient.getMapCache("user-center:imageCode");
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        final String code = lineCaptcha.getCode();
        final String imageBase64Data = lineCaptcha.getImageBase64Data();
        log.info("验证码是{} imageCodeTraceId is {}", code, imageCodeTraceId);
        mapCache.put(imageCodeTraceId, code, 5, TimeUnit.MINUTES);
        return Response.success(imageBase64Data);
    }

    @PostMapping("/login")
    public Response<LoginRspDto> login(@RequestBody @Valid LoginReqDto loginReqDto) {
        RMapCache<String, String> mapCache = redissonClient.getMapCache("user-center:imageCode");
        final String imageCode = mapCache.get(loginReqDto.getImageCodeTraceId());
        if (StringUtil.isBlank(imageCode)) {
            return Response.error(GlobalErrorCodeConstants.imageCodeError);
        }
        if (!imageCode.equalsIgnoreCase(loginReqDto.getImageCode())) {
            return Response.error(GlobalErrorCodeConstants.imageCodeError);
        }
        /*验证码校验通过，删除缓存*/
        mapCache.remove(loginReqDto.getImageCodeTraceId());
        final UserInfo userInfo = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getLoginName, loginReqDto.getUsername()));
        if (null == userInfo) {
            return Response.error(GlobalErrorCodeConstants.userNameOrPasswordError);
        }
        final String hashpw = BCrypt.hashpw(loginReqDto.getPassword(), userInfo.getSalt());
        if (!hashpw.equals(userInfo.getPassword())) {
            return Response.error(GlobalErrorCodeConstants.userNameOrPasswordError);
        }
        StpUtil.login(userInfo.getId());
        final SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LoginRspDto loginRsp = new LoginRspDto();
        loginRsp.setToken(tokenInfo.getTokenValue());
        loginRsp.setUsername(userInfo.getUserName());
        loginRsp.setTokenName(tokenInfo.getTokenName());
        return Response.success(loginRsp);
    }
    public static void main(String[] args) {
        final String gensalt = BCrypt.gensalt();
        System.out.println(gensalt);
        String pw_hash = BCrypt.hashpw("liu821290375", "$2a$10$EyUsJ2hvGDj5gmwFfeKnSe");
        System.out.println(pw_hash);

    }
}
