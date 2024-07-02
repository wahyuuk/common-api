package co.id.kuncoro.commonapi.util;

import co.id.kuncoro.commonapi.constant.CacheKey;
import co.id.kuncoro.commonapi.constant.ErrorCode;
import co.id.kuncoro.commonapi.constant.ValidationMessage;
import co.id.kuncoro.commonapi.dto.ErrorValidationDto;
import co.id.kuncoro.commonapi.dto.parameter.validationmessage.ValidationMessageResponseDto;
import co.id.kuncoro.commonapi.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
@RequiredArgsConstructor
public class ValidationUtils {

  private final CacheUtils cacheUtils;

  public ServiceException throwValidation(HttpServletRequest servletRequest, Map<String, Object> errorMaps) {
    var errors = CollectionUtils.emptyIfNull(errorMaps.entrySet())
        .stream()
        .map(e -> ErrorValidationDto.builder().build())
        .toList();
    return new ServiceException(ErrorCode.UNPROCESSABLE_ENTITY, errors);
  }

  public ServiceException throwValidation(List<ErrorValidationDto> errors) {
    return new ServiceException(ErrorCode.UNPROCESSABLE_ENTITY, errors);
  }

  public ErrorValidationDto getError(ValidationMessage message, String fieldName, Locale locale, Object... args) {
    var error = cacheUtils.get(CacheKey.VALIDATION_MESSAGE, message.name(), ValidationMessageResponseDto.class);
    return Optional.ofNullable(error)
        .map(e -> ErrorValidationDto.builder()
            .field(fieldName)
            .rejectedValue(e)
            .build())
        .orElse(null);
  }

  public List<ErrorValidationDto> getErrors(MethodArgumentNotValidException exception, Locale locale) {
    var errorsCodes = CollectionUtils.emptyIfNull(exception.getFieldErrors())
        .stream()
        .flatMap(e -> Arrays.stream(e.getCodes()))
        .collect(Collectors.toSet());
    var errors = cacheUtils.multiGet(CacheKey.VALIDATION_MESSAGE, errorsCodes, ValidationMessageResponseDto.class);
    return CollectionUtils.emptyIfNull(exception.getFieldErrors())
        .stream()
        .map(e -> ErrorValidationDto.builder()
            .field(e.getField())
            .rejectedValue(e.getRejectedValue())
            .message(errors.stream()
                .filter(error -> Stream.of(e.getCodes()).anyMatch(code -> StringUtils.equals(error.getCode(), code)))
                .map(error -> error.getMessage(locale))
                .toList())
            .build())
        .toList();
  }

}
