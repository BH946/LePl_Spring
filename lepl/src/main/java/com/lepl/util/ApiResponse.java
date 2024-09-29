package com.lepl.util;

import lombok.Getter;
import org.checkerframework.checker.units.qual.A;
import org.springframework.validation.BindingResult;

/**
 * "API 응답 양식 지정"
 * 컨트롤러에서 Valid + 제네릭(타입문제 해결) 목적
 */
@Getter
public class ApiResponse<T> {

  private int status;
  private String flag;
  private T data;
  private String message;
  private Object rejectValue;
  private String errorObject;

  public ApiResponse(int status, String flag, T data, String message, Object rejectValue, String errorObject) {
    this.status = status;
    this.flag = flag;
    this.data = data;
    this.message = message;
    this.rejectValue = rejectValue;
    this.errorObject = errorObject;
  }

  public static <T> ApiResponse<T> success(int status, T data) {
    return new ApiResponse<>(status, "정상", data, null, null, null);
  }

  public static <T> ApiResponse<T> error(int status, BindingResult bindingResult) {
    return new ApiResponse<>(status,
        "검증 오류",
        null,
        bindingResult.getFieldError().getDefaultMessage(),
        bindingResult.getFieldError().getRejectedValue(),
        bindingResult.getObjectName()+"."+bindingResult.getFieldError().getField());
  }

  public static <T> ApiResponse<T> errorObject(int status, BindingResult bindingResult) {
    return new ApiResponse<>(status,
        "검증 오류",
        null,
        bindingResult.getGlobalError().getDefaultMessage(),
        null,
        bindingResult.getGlobalError().getObjectName()
        );
  }
}
