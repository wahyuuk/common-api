package co.id.kuncoro.commonapi.exception;

import static co.id.kuncoro.commonapi.constant.Constants.KEY_SPARATOR;
import static org.apache.commons.lang3.StringUtils.join;

import co.id.kuncoro.commonapi.constant.CacheKey;
import co.id.kuncoro.commonapi.constant.ErrorCode;
import co.id.kuncoro.commonapi.dto.ErrorResponseDto;
import co.id.kuncoro.commonapi.dto.ErrorValidationDto;
import co.id.kuncoro.commonapi.dto.parameter.errorcode.ErrorCodeResponseDto;
import co.id.kuncoro.commonapi.util.CacheUtils;
import co.id.kuncoro.commonapi.util.LanguageUtils;
import co.id.kuncoro.commonapi.util.TracerUtils;
import co.id.kuncoro.commonapi.util.ValidationUtils;
import io.netty.handler.timeout.TimeoutException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final TracerUtils tracerUtils;
  private final ValidationUtils validationUtils;
  private final CacheUtils cacheUtils;

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<ErrorResponseDto<Object>> handleServiceException(ServiceException e,
      HttpServletRequest servletRequest) {
    var errorCode = e.getErrorCode();
    var response = constructErrorResponseDto(getError(e.getErrorCode()), servletRequest);
    response.setPath(servletRequest.getPathInfo());
    if (e.getErrors() != null) {
      errorCode = ErrorCode.UNPROCESSABLE_ENTITY;
      response.setErrors(e.getErrors());
    }

    return ResponseEntity.status(errorCode.getStatus())
        .body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {
    var errors = validationUtils.getErrors(e, request.getLocale());
    var response = constructErrorResponseDto(getError(ErrorCode.UNPROCESSABLE_ENTITY), request.getLocale(), errors);
    var servletRequest = (ServletWebRequest) request;
    response.setPath(Optional.ofNullable(servletRequest)
        .map(ServletWebRequest::getRequest)
        .map(HttpServletRequest::getPathInfo)
        .orElse(null));

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(response);
  }

  @ExceptionHandler(RestTemplateException.class)
  public ResponseEntity<ErrorResponseDto<Object>> handleRestTemplateException(RestTemplateException e,
      HttpServletRequest servletRequest) {
    var response = constructErrorResponseDto(getError(e.getCode(), e.getSourceSystem()), servletRequest);
    response.setPath(servletRequest.getPathInfo());
    var errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    if (e.getCause() instanceof TimeoutException) {
      errorStatus = HttpStatus.GATEWAY_TIMEOUT;
    }

    if (e.getCause() instanceof ConnectException) {
      errorStatus = HttpStatus.SERVICE_UNAVAILABLE;
    }

    return ResponseEntity.status(errorStatus)
        .body(response);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponseDto<Object>> handleDataAccessException(DataAccessException e,
      HttpServletRequest servletRequest) {
    var response = constructErrorResponseDto(getError(ErrorCode.INTERNAL_SERVER_ERROR), servletRequest);
    response.setPath(servletRequest.getPathInfo());

    return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(response);
  }

  private ErrorResponseDto<Object> constructErrorResponseDto(ErrorCodeResponseDto error,
      HttpServletRequest servletRequest) {
    return constructErrorResponseDto(error, LanguageUtils.getLocale(servletRequest), null);
  }

  private ErrorResponseDto<Object> constructErrorResponseDto(ErrorCodeResponseDto error, Locale locale,
      List<ErrorValidationDto> errors) {
    return ErrorResponseDto.builder()
        .activityRefCode(tracerUtils.getTraceId())
        .sourceSystem(error.getSourceSystem())
        .code(error.getCode())
        .title(error.getTitle(locale))
        .message(error.getMessage(locale))
        .errors(errors)
        .build();
  }

  private ErrorCodeResponseDto getError(ErrorCode errorCode) {
    return cacheUtils.get(CacheKey.ERROR_CODE, errorCode.getHashKey(), ErrorCodeResponseDto.class);
  }

  private ErrorCodeResponseDto getError(String code, String sourceSystem) {
    return cacheUtils.get(CacheKey.ERROR_CODE, join(code, KEY_SPARATOR, sourceSystem), ErrorCodeResponseDto.class);
  }

}
