package com.project.eat.Register;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor // @autowired 선언을 생략하고 전역변수에대해서 자동DI를 해준다.
public class RegisterMvcConfig implements WebMvcConfigurer {

    private final RegisterInterceptor registerInterceptor;
    private final RegisterRestInterceptor registerRestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // interceptor를 여러개 지정할 수 있다. 메소드 순서대로 실행
        registry.addInterceptor(registerInterceptor).addPathPatterns("/member/*");
        registry.addInterceptor(registerRestInterceptor).addPathPatterns("/api/member/*");
    }
}
