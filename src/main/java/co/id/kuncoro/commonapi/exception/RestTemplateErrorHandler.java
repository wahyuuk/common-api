package co.id.kuncoro.commonapi.exception;

import co.id.kuncoro.commonapi.dto.ErrorResponseDto;
import co.id.kuncoro.commonapi.util.MapperUtils;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
@RequiredArgsConstructor
public class RestTemplateErrorHandler implements ResponseErrorHandler {

  private final MapperUtils mapperUtils;

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError();
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    if (this.hasError(response)) {
      var body = mapperUtils.readValue(response.getBody(), ErrorResponseDto.class);
      throw new RestTemplateException(body.getCode(), body.getSourceSystem());
    }
  }

}
