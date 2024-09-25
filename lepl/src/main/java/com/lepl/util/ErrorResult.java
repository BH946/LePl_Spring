package com.lepl.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 에외처리 응답 양식
 */
@Data
public class ErrorResult {

  private String code;
  private String message;

  public ErrorResult(String code, String message) {
    this.code=code;
    this.message=message;
  }
}
