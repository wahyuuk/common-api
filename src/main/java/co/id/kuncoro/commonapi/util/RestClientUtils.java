package co.id.kuncoro.commonapi.util;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestClientUtils {

  private RestTemplate restTemplate;

  public <T> T get(String url, Class<T> responseClass) {
    var response = restTemplate.getForEntity(url, responseClass);
    return response.getBody();
  }

  public <T> T get(String url) {
    var response = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<T>() {});
    return response.getBody();
  }

  public <T, P> T get(String url, P params, Class<P> paramsClass, Object... uriVariables) {
    var uri = ParameterQueryUtils.buildQueryParam(params, url, paramsClass, uriVariables);
    var response = restTemplate.exchange(uri.toUri(), GET, null, new ParameterizedTypeReference<T>() {});
    return response.getBody();
  }

  public <T, R> T post(String url, R request) {
    var response = restTemplate.exchange(url, GET, new HttpEntity<>(request), new ParameterizedTypeReference<T>() {});
    return response.getBody();
  }

  public <T, R> T post(String url, R request, Class<T> responseClass) {
    var response = restTemplate.postForEntity(url, request, responseClass);
    return response.getBody();
  }

  public <T, R> T post(String url, R request, Class<T> responseClass, Object... uriVariables) {
    var response = restTemplate.postForEntity(url, request, responseClass, uriVariables);
    return response.getBody();
  }

  public <T, R> T put(String url, R request, Class<T> responseClass, Object... uriVariables) {
    var response = restTemplate.exchange(url, PUT, new HttpEntity<>(request), responseClass, uriVariables);
    return response.getBody();
  }

}
