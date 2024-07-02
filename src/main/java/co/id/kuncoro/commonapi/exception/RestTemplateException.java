package co.id.kuncoro.commonapi.exception;

import lombok.Getter;

@Getter
public class RestTemplateException extends RuntimeException {

  private static final long serialVersionUID = 4255769184968059784L;

  private final String code;
  private final String sourceSystem;

  public RestTemplateException(String code, String sourceSystem) {
    this.code = code;
    this.sourceSystem = sourceSystem;
  }

}
