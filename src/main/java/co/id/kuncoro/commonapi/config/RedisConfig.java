package co.id.kuncoro.commonapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

  @Bean
  RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
      StringRedisSerializer stringSerializer, Jackson2JsonRedisSerializer<Object> objectSerializer) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(stringSerializer);
    template.setValueSerializer(objectSerializer);
    template.setHashKeySerializer(objectSerializer);
    template.setHashValueSerializer(objectSerializer);
    return template;
  }

  @Bean
  StringRedisSerializer stringSerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  Jackson2JsonRedisSerializer<Object> objectSerializer(ObjectMapper mapper) {
    return new Jackson2JsonRedisSerializer<>(mapper, Object.class);
  }

}
