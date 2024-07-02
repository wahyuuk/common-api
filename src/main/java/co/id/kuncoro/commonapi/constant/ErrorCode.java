package co.id.kuncoro.commonapi.constant;

import static co.id.kuncoro.commonapi.constant.Constants.KEY_SPARATOR;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  SUCCESS(HttpStatus.OK, "APP", "200"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "APP", "500"),
  UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "APP", "422"),
  NOT_FOUND(HttpStatus.BAD_REQUEST, "APP", "404");

  private HttpStatus status;
  private String sourceSystem;
  private String code;

  ErrorCode(HttpStatus status, String sourceSystem, String code) {
    this.status = status;
    this.code = code;
    this.sourceSystem = sourceSystem;
  }

  public String getHashKey() {
    return StringUtils.join(sourceSystem, KEY_SPARATOR, code);
  }

}
