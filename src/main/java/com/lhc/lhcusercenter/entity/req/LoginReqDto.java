package com.lhc.lhcusercenter.entity.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * Author: 刘海潮
 * Date: 2024/8/23 0:20
 */
@Data
public class LoginReqDto implements Serializable {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "imageCodeTraceId不能为空")
    private String imageCodeTraceId;
    @NotBlank(message = "验证码不能为空")
    private String imageCode;
}
