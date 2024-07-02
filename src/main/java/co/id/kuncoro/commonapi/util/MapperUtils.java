package co.id.kuncoro.commonapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperUtils {

  private final ObjectMapper objectMapper;

  public String writeValueAsString(Object object) {
    return writeValueAsString(object, false);
  }

  public String writeValueAsString(Object object, boolean isPrettyPrint) {
    if (object == null) {
      return null;
    }

    try {
      if (isPrettyPrint) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
      }

      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public <T> T readValue(InputStream content, Class<T> valueType) {
    return readValue(content, valueType, null);
  }

  public <T> T readValue(String content, Class<T> valueType) {
    return readValue(content, valueType, null);
  }

  public <T> T readValue(String content, Class<T> valueType, T defaultValue) {
    return readValue(content, objectMapper.getTypeFactory().constructType(valueType), defaultValue);
  }

  public <T> T readValue(String content, TypeReference<T> valueTypeRef) {
    return readValue(content, valueTypeRef, null);
  }

  public <T> T readValue(String content, TypeReference<T> valueTypeRef, T defaultValue) {
    return readValue(content, objectMapper.getTypeFactory().constructType(valueTypeRef), defaultValue);
  }

  public <T> T readValue(InputStream content, Class<T> valueType, T defaultValue) {
    try {
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return objectMapper.readValue(content, valueType);
    } catch (IOException e) {
      return defaultValue;
    }
  }

  private <T> T readValue(String content, JavaType valueType, T defaultValue) {
    if (StringUtils.isEmpty(content)) {
      return defaultValue;
    }

    try {
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return objectMapper.readValue(content, valueType);
    } catch (IOException e) {
      return defaultValue;
    }
  }

  public String writeAsJsonString(String content) {
    return writeAsJsonString(content, false);
  }

  public String writeAsJsonString(String content, boolean isPrettyPrint) {
    if (StringUtils.isEmpty(content)) {
      return content;
    }

    try {
      var stringObject = objectMapper.readValue(content, Object.class);
      return writeValueAsString(stringObject, isPrettyPrint);
    } catch (IOException e) {
      return content;
    }
  }

}
