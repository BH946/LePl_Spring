package com.lvpl.api;

import com.lvpl.api.argumentresolver.LoginMemberArgumentResolver;
import com.lvpl.api.interceptor.MemberCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration // 설정 파일임을 알림
@Slf4j
public class ApiConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberCheckInterceptor())
                .order(2)
                .addPathPatterns("/**") // 모든 경로 접근
                .excludePathPatterns("/", "/api/v1/members/login", "/api/v1/members/register",
                        "/api/v1/members/logout","/css/**","/*.ico","/error"); // 제외 경로!

    }
}
