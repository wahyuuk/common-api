package co.id.kuncoro.commonapi.util;

import java.lang.reflect.Field;
import java.util.Objects;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class ParameterQueryUtils {

  private ParameterQueryUtils() {
  }

  public static <T> UriComponents buildQueryParam(T params, String url, Class<T> clazz) {
    var uriBuilder = UriComponentsBuilder.fromUriString(url);
    for (Field field : FieldUtils.getAllFields(clazz)) {
      var value = readDeclaredField(params, field.getName());
      if (value != null) {
        uriBuilder.queryParam(field.getName(), value);
      }
    }

    return uriBuilder.build();
  }

  public static <T> UriComponents buildQueryParam(T params, String url, Class<T> clazz, Object... values) {
    var uriBuilder = UriComponentsBuilder.fromUriString(url);
    for (Field field : FieldUtils.getAllFields(clazz)) {
      var value = readDeclaredField(params, field.getName());
      if (value != null) {
        uriBuilder.queryParam(field.getName(), value);
      }
    }

    return uriBuilder.buildAndExpand(values);
  }

  public static UriComponents buildQueryParam(String key, Object value, String url) {
    var uriBuilder = UriComponentsBuilder.fromUriString(url);
    if (!Objects.isNull(value)) {
      uriBuilder.queryParam(key, value);
    }

    return uriBuilder.build();
  }

  private static <T> Object readDeclaredField(T object, String fieldName) {
    try {
      return FieldUtils.readField(object, fieldName, true);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      return null;
    }
  }

}
