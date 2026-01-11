package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理端jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor extends JwtTokenInterceptor {

    // 从请求头中获取管理员令牌
    @Override
    protected String getAndTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(jwtProperties.getAdminTokenName());
    }

    // 校验管理员令牌，并将empId存入ThreadLocal中
    @Override
    protected void checkToken(String token) {
        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
        Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
        log.info("id={}的员工登入苍穹外卖管理端\n", empId);
        BaseContext.setCurrentId(empId);
    }
}
