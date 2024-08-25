package com.lhc.lhcusercenter.common.exception;

import com.lhc.lhcusercenter.common.pojo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

/**
 * Author: 刘海潮
 * Date: 2024/8/25 11:01
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleAllExceptions(Exception ex) {
        log.error("1报错信息:", ex);
        // 记录日志，处理异常信息
        return Response.error(400, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        log.error("报错信息:", ex);
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (CollectionUtils.isEmpty(fieldErrors)) {
            return Response.error(400, ex.getMessage());
        }
        final String join = String.join(",", fieldErrors.stream().map(FieldError::getDefaultMessage).toList());
        return Response.error(400, join);
    }
}
