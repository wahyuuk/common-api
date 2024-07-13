package co.id.kuncoro.commonapi.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {
    loggingOutgoinRequest(request, body);
    var response = execution.execute(request, body);
    loggingOutgoinResponse(response);

    return response;
  }

  private void loggingOutgoinRequest(HttpRequest request, byte[] body) {
    if (log.isDebugEnabled()) {
      log.debug("------------------------------------- OUTGOING Request Begin --------------------------------------");
      log.debug("URI         : {}", request.getURI());
      log.debug("Method      : {}", request.getMethod());
      log.debug("Headers     : {}", request.getHeaders());
      log.debug("Body        : {}", new String(body, StandardCharsets.UTF_8));
      log.debug("-------------------------------------- OUTGOING Request End ---------------------------------------");
    }
  }

  private void loggingOutgoinResponse(ClientHttpResponse response) throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("------------------------------------- OUTGOING Response Begin --------------------------------------");
      var strBuilder = new StringBuilder();
      var bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
      var line = bufferedReader.readLine();
      while (line != null) {
        strBuilder.append(line);
        strBuilder.append(System.lineSeparator());
        line = bufferedReader.readLine();
      }

      log.debug("Status       : {}", response.getStatusCode());
      log.debug("Headers      : {}", response.getHeaders());
      log.debug("Body         : {}", strBuilder.toString());

      log.debug("-------------------------------------- OUTGOING Response End ---------------------------------------");
    }
  }

}
