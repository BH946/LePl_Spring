//package com.lepl.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.Data;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//  private ObjectMapper objectMapper = new ObjectMapper();
//
//  public CustomAuthenticationFilter() {
//    // url과 일치하면 해당 필터가 가로채서 동작!
//    super(new AntPathRequestMatcher("/api/v1/members/login"));
//    System.out.println("test2");
//  }
//
//  @Override
//  public Authentication attemptAuthentication(HttpServletRequest request,
//      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//    // POST 인지 확인
//    System.out.println("test1");
//    if (!"POST".equals(request.getMethod())) {
//      throw new IllegalStateException("Authentication is not supported, no post");
//    }
//    // body를 login 정보에 매핑
//    LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
//    // id, password cehck
//    if (loginDto.getUid().isEmpty()) {
//      throw new IllegalArgumentException("username or password is empty, no uid");
//    }
//    // 인증 되지 않은 토큰 생성 (나중에 인증)
//    CustomAuthenticationToken token = new CustomAuthenticationToken(
//        loginDto.getUid(),
//        "" // uid만 사용하므로 password 생략
//    );
//    // manager 가 인증 처리하게 위임
//    Authentication authentication = getAuthenticationManager().authenticate(token);
//    return authentication;
//  }
//
//  @Data
//  public static class LoginDto {
//    private String uid;
//  }
//}
