package co.id.kuncoro.commonapi.util;

import co.id.kuncoro.commonapi.constant.CacheKey;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheUtils {

  private final RedisTemplate<String, Object> redisTemplate;
  private final MapperUtils mapperUtils;

  public HashOperations<String, String, Object> opsForHash() {
    return redisTemplate.opsForHash();
  }

  public ValueOperations<String, Object> opsForValue() {
    return redisTemplate.opsForValue();
  }

  public <T> List<T> getAll(CacheKey key, Class<T> clazz) {
    if (key == null) {
      return List.of();
    }

    return CollectionUtils.emptyIfNull(opsForHash().values(key.name()))
        .stream()
        .map(e -> objectToClass(e, clazz))
        .toList();
  }

  public <T> List<T> multiGet(CacheKey key, Collection<String> hashKeys, Class<T> clazz) {
    if (hashKeys == null || hashKeys.isEmpty()) {
      return List.of();
    }

    return CollectionUtils.emptyIfNull(opsForHash().multiGet(key.name(), hashKeys))
        .stream()
        .map(e -> objectToClass(e, clazz))
        .toList();
  }

  public <T> List<T> multiGet(List<String> keys, Class<T> clazz) {
    if (keys == null || keys.isEmpty()) {
      return List.of();
    }

    return CollectionUtils.emptyIfNull(opsForValue().multiGet(keys))
        .stream()
        .map(e -> objectToClass(e, clazz))
        .toList();
  }

  public <T> T get(CacheKey key, String hashKey, Class<T> clazz) {
    if (key == null) {
      return null;
    }

    return Optional.ofNullable(opsForHash().get(key.name(), hashKey))
        .map(e -> objectToClass(e, clazz))
        .orElse(null);
  }

  public <T> T get(CacheKey key, String hashKey, TypeReference<T> typeReference) {
    if (key == null) {
      return null;
    }

    return Optional.ofNullable(opsForHash().get(key.name(), hashKey))
        .map(e -> objectToClass(e, typeReference))
        .orElse(null);
  }

  public <T> T get(String key, Class<T> clazz) {
    if (key == null) {
      return null;
    }

    return Optional.ofNullable(opsForValue().get(key))
        .map(e -> objectToClass(e, clazz))
        .orElse(null);
  }

  public <T> T get(String key, TypeReference<T> typeReference) {
    if (key == null) {
      return null;
    }

    return Optional.ofNullable(opsForValue().get(key))
        .map(e -> objectToClass(e, typeReference))
        .orElse(null);
  }

  public <T> void put(CacheKey key, String hashKey, T value) {
    opsForHash().put(key.name(), hashKey, value);
  }

  public <T> void put(String key, T value) {
    opsForValue().set(key, value);
  }

  public <T> void put(String key, T value, Duration duration) {
    opsForValue().set(key, value, duration);
  }

  public <T> void putAll(CacheKey key, Map<String, T> values) {
    opsForHash().putAll(key.name(), values);
  }

  public <T> void putAll(Map<String, T> values) {
    opsForValue().multiSet(values);
  }

  private <T> T objectToClass(Object value, Class<T> clazz) {
    var valueStr = mapperUtils.writeValueAsString(value);
    return mapperUtils.readValue(valueStr, clazz);
  }

  private <T> T objectToClass(Object value, TypeReference<T> typeReference) {
    var valueStr = mapperUtils.writeValueAsString(value);
    return mapperUtils.readValue(valueStr, typeReference);
  }

}
