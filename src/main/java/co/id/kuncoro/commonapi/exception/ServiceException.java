package co.id.kuncoro.commonapi.exception;

import co.id.kuncoro.commonapi.constant.ErrorCode;
import co.id.kuncoro.commonapi.dto.ErrorValidationDto;
import java.util.List;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = -1722284027759003217L;

  private final ErrorCode errorCode;
  private final transient List<ErrorValidationDto> errors;

  public ServiceException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.errors = null;
  }

  public ServiceException(ErrorCode errorCode, List<ErrorValidationDto> errors) {
    this.errorCode = errorCode;
    this.errors = errors;
  }

}
