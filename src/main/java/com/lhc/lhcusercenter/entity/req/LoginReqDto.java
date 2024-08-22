package com.lhc.lhcusercenter.entity.req;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: 刘海潮
 * Date: 2024/8/23 0:20
 */
@Data
public class LoginReqDto implements Serializable {
    private String username;
    private String password;
    private String imageCodeTraceId;
    private String imageCode;
}
