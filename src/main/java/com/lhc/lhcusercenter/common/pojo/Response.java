package com.lhc.lhcusercenter.common.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lhc.lhcusercenter.common.exception.ErrorCode;
import com.lhc.lhcusercenter.common.exception.ServiceException;
import com.lhc.lhcusercenter.common.exception.enums.GlobalErrorCodeConstants;
import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * 通用返回
 *
 * @param <T> 数据泛型
 */
@Data
public class Response<T> implements Serializable {

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    private Integer code;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    private String msg;

    /**
     * 将传入的 result 对象，转换成另外一个泛型结果的对象
     *
     * 因为 A 方法返回的 CommonResult 对象，不满足调用其的 B 方法的返回，所以需要进行转换。
     *
     * @param response 传入的 result 对象
     * @param <T>    返回的泛型
     * @return 新的 CommonResult 对象
     */
    public static <T> Response<T> error(Response<?> response) {
        return error(response.getCode(), response.getMsg());
    }

    public static <T> Response<T> error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        Response<T> response = new Response<>();
        response.code = code;
        response.msg = message;
        return response;
    }

    public static <T> Response<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        response.data = data;
        response.msg = "";
        return response;
    }
    public static <T> Response<T> success() {
        Response<T> response = new Response<>();
        response.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        response.msg = "";
        return response;
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(code);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    // ========= 和 Exception 异常体系集成 =========

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(code, msg);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #data} 数据
     */
    @JsonIgnore // 避免 jackson 序列化
    public T getCheckedData() {
        checkError();
        return data;
    }

    public static <T> Response<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

}
