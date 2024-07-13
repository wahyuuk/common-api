package co.id.kuncoro.commonapi.config;

import co.id.kuncoro.commonapi.exception.RestTemplateErrorHandler;
import co.id.kuncoro.commonapi.filter.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

  private final RestTemplateErrorHandler errorHandler;

  @Bean
  RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.errorHandler(errorHandler)
        .setConnectTimeout(null)
        .setReadTimeout(null)
        .interceptors(new LoggingInterceptor())
        .build();
  }

}
