//package com.lepl.security;
//
//import static com.lepl.util.Messages.SUCCESS_LOGIN;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lepl.domain.member.Member;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//  private ObjectMapper objectMapper = new ObjectMapper();
//
//  //Rest API 이기 때문에 redirect 처리를 하지 않고 response 에 바로 값
//  @Override
//  public void onAuthenticationSuccess(HttpServletRequest request,
//      HttpServletResponse response,
//      Authentication authentication) throws IOException {
//    System.out.println("test3");
//    MemberDetail member = (MemberDetail) authentication.getPrincipal();
//
//    response.setStatus(HttpStatus.OK.value());
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
////    objectMapper.writeValue(response.getWriter(), member);
//    objectMapper.writeValue(response.getWriter(), SUCCESS_LOGIN);
//  }
//}
