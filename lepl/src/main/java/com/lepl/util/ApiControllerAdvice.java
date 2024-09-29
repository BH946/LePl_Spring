package com.lepl.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.lepl.api") //컨트롤러 예외들 여기서 처리하게끔 역할
public class ApiControllerAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST) //이거 덕분에 ResponseEntity 없이 응답 코드 설정! (간단하게 하려고 추가함)
  @ExceptionHandler(IllegalArgumentException.class) //해당 예외가 잡히면 이 함수를 실행 역할!
  public ErrorResult illegalApiHandler(IllegalArgumentException e) {
    log.error("[exceptionHandler] ", e);
    return new ErrorResult("BAD REQUEST", e.getMessage());
  }

  @ResponseStatus(HttpStatus.CONFLICT) //중복 충돌이니까:409
  @ExceptionHandler(IllegalStateException.class)
  public ErrorResult illegalApiHandler(IllegalStateException e) {
    log.error("[exceptionHandler] ", e);
    return new ErrorResult("CONFLICT", e.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  public ErrorResult exceptionApiHandler(Exception e) {
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("SERVER ERROR", e.getMessage());
  }
}
