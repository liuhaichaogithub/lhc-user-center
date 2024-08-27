package com.lhc.lhcusercenter.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhc.lhcusercenter.entity.rsp.LoginRspDto;
import org.springframework.boot.json.JsonParseException;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Author: 刘海潮
 * Date: 2024/8/26 下午5:07
 */
public class JacksonUtil {
    private JacksonUtil() {
    }

    // 静态代码块单例
    private final static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static <T> T tryParse(Callable<T> parser) {
        return tryParse(parser, JacksonException.class);
    }

    public static <T> T tryParse(Callable<T> parser, Class<? extends Exception> check) {
        try {
            return parser.call();
        } catch (Exception ex) {
            if (check.isAssignableFrom(ex.getClass())) {
                throw new JsonParseException(ex);
            }
            throw new IllegalStateException(ex);
        }
    }


    public static void main(String[] args) {
        final LoginRspDto loginRspDto = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().readValue("{\"token\":\"asdasd\",\"username\":\"admin\",\"tokenName\":\"token\"}", LoginRspDto.class));
        System.out.println(loginRspDto.getUsername());
//        final List<LoginRspDto> loginRspDtos = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().readValue("[{\"token\":\"asdasd\",\"username\":\"admin\",\"tokenName\":\"token\"},{\"token\":\"asdasd\",\"小明\":\"admin\",\"tokenName\":\"token\"}]", new TypeReference<List<LoginRspDto>>() {}));
//        System.out.println(loginRspDtos.get(1).getUsername());

    }
}

