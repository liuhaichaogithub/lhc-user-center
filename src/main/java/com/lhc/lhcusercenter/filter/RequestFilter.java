package com.lhc.lhcusercenter.filter;

import com.alibaba.fastjson2.JSON;
import com.lhc.lhcusercenter.common.exception.enums.GlobalErrorCodeConstants;
import com.lhc.lhcusercenter.common.pojo.Response;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Author: 刘海潮
 * Date: 2024/8/16 下午5:04
 */
@WebFilter(filterName = "WebFilter", urlPatterns = "/*")
@Order(1)
@Slf4j
public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("WebFilter 过滤器初始化。。。。");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        doFilterInternal(request, response, chain);

    }

    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String uri = request.getRequestURI();
        final String traceId = request.getHeader("traceId");
        log.info("请求地址是 {} traceId {} ", uri, traceId);
        if (StringUtils.isEmpty(traceId)) {
            takeRsp(response, Response.error(GlobalErrorCodeConstants.TRACE_ID_NONE));
            return;
        }

//        Map<String, String[]> parameterMap = request.getParameterMap();
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


    public void takeRsp(HttpServletResponse servletResponse, Response<?> response) throws IOException {
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json;charset=utf-8");
        servletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        ServletOutputStream out = servletResponse.getOutputStream();
        out.write(JSON.toJSONString(response).getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
