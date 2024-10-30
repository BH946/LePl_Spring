package com.lepl.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


/**
 * Spring Security 쓸거면??
 * 1. build.gradle 에 주석해제해서 라이브러리 다운
 * 2. ApiConfigV1.java 의 argumentresolver, interceptor 메소드 주석!!
 * 3. security 패키지 파일 주석 전부 해제 (개노가다;;)
 */
@Configuration // 설정 파일임을 알림
@EnableWebSecurity // Spring Security
@RequiredArgsConstructor
@Slf4j
public class ApiConfigV2 {
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final HandlerMappingIntrospector introspector;
  private final MemberDetailService detailService;

  /**
   * Spring Security의 앞단 설정을 할수 있다.
   * debug, firewall, ignore등의 설정이 가능
   * 단 여기서 resource에 대한 모든 접근을 허용하는 설정할수도 있는데
   * 그럴경우 SpringSecuity에서 접근을 통제하는 설정은 무시해버린다.
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> {
      web
          .ignoring()
          .requestMatchers(new AntPathRequestMatcher("/h2-console/**")
          );
    };
  }

  /**
   * Spring Security 기능 설정을 할수 있다.
   * 특정 리소스에 접근하지 못하게 하거나 반대로 로그인, 회원가입 페이지외에 인증정보가 있어야
   * 접근할 수 있도록 설정할 수 있다.
   * 특정 리소스의 접근허용 또는 특정 권한 요구,로그인, 로그아웃, 로그인,로그아웃 성공시 Event
   * 등의 설정이 가능하다.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // csrf는 브라우저 없이 API 개발은 jwt같은 인증방식을 사용해서 굳이 필요가 없다고 한다.
    // 요청에 대한 설정
    // permitAll시 해당 url에 대한 인증정보를 요구하지 않는다.
    // authenticated시 해당 url에는 인증 정보를 요구한다.(로그인 필요)
    // hasAnyRole시 해당 url에는 특정 권한 정보를 요구한다.
    // resources에 대해 접근혀용을 해야지 브라우저에서 로그인없이 js파일이나 css파일에 접근할 수 있다.
    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(new AntPathRequestMatcher("/**"))
        ) //전체 경로에 csrf 비활성화
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(new MvcRequestMatcher(introspector,"/api/v1/members/**")).permitAll()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 자원
            .anyRequest().authenticated()
        )
        .addFilterBefore(ajaxAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class) // 필터 가로채기!
        .logout(logout -> logout
            .logoutUrl("/api/v1/members/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
              response.setStatus(HttpStatus.OK.value());
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              response.getWriter().print("로그아웃 성공");
            }) // 로그아웃 성공 핸들러 (간단히 추가)
        );
    return http.build();
  }

  @Bean
  public CustomAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
    customAuthenticationFilter.setAuthenticationManager(authenticationManager()); //manager 등록
    customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
    customAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

    //Form Login 방식은 기본적으로 DelegatingSecurityContextRepository 가 설정되어 있어서 REST API 방식을 위해 아래 추가
    //RequestAttributeSecurityContextRepository 는 세션을 저장 하지 않기 때문에 로그인 후 API 요청시 Anonymous 토큰이 생성되게 됩니다.
    //HttpSessionSecurityContextRepository 는 빈 세션을 만들 뿐! (어차피 토큰 사용할거라 빈 상태로 놔둘거임)
    customAuthenticationFilter.setSecurityContextRepository(
        new DelegatingSecurityContextRepository(
            new RequestAttributeSecurityContextRepository(),
            new HttpSessionSecurityContextRepository()
        ));
    System.out.println("test5");
    return customAuthenticationFilter;
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(); //provider 에서 사용 위해 빈 등록
  }
}
