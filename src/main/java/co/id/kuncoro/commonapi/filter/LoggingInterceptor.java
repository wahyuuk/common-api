package co.id.kuncoro.commonapi.filter;

import static co.id.kuncoro.commonapi.constant.Constants.LINE_SPARATOR;

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
      var sb = new StringBuilder(LINE_SPARATOR);
      sb.append("------------------------------------- OUTGOING Request Begin --------------------------------------")
          .append(LINE_SPARATOR);
      sb.append("Path        : ").append(request.getURI()).append(LINE_SPARATOR);
      sb.append("Method      : ").append(request.getMethod()).append(LINE_SPARATOR);
      sb.append("Headers     : ").append(request.getHeaders()).append(LINE_SPARATOR);
      sb.append("Body        : ").append(new String(body, StandardCharsets.UTF_8)).append(LINE_SPARATOR);
      sb.append("-------------------------------------- OUTGOING Request End ---------------------------------------")
          .append(LINE_SPARATOR);
      log.debug(sb.toString());
    }
  }

  private void loggingOutgoinResponse(ClientHttpResponse response) throws IOException {
    if (log.isDebugEnabled()) {
      var sb = new StringBuilder();
      sb.append("------------------------------------- OUTGOING Response Begin --------------------------------------")
          .append(LINE_SPARATOR);
      sb.append("Status       : ").append(response.getStatusCode());
      sb.append("Headers      : ").append(response.getHeaders());
      var bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
      var line = bufferedReader.readLine();

      var sbBody = new StringBuilder();
      while (line != null) {
        sbBody.append(line);
        sbBody.append(LINE_SPARATOR);
        line = bufferedReader.readLine();
      }

      sb.append("Body         : ").append(sbBody.toString());
      sb.append("-------------------------------------- OUTGOING Response End ---------------------------------------");
      log.debug(sb.toString());
    }
  }

}
