package com.lhc.lhcusercenter.entity.rsp;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: 刘海潮
 * Date: 2024/8/23 下午12:37
 */
@Data
public class LoginRspDto implements Serializable {
    private String token;
    private String username;
    private String tokenName;
}
