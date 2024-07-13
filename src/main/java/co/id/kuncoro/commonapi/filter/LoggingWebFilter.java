package co.id.kuncoro.commonapi.filter;

import static co.id.kuncoro.commonapi.constant.Constants.LINE_SPARATOR;

import co.id.kuncoro.commonapi.util.MapperUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingWebFilter extends OncePerRequestFilter {

  private final MapperUtils mapperUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var requestWrapper = new ContentCachingRequestWrapper(request);
    var responseWrapper = new ContentCachingResponseWrapper(response);
    
    var startTime = System.currentTimeMillis();
    filterChain.doFilter(requestWrapper, responseWrapper);
    var timeTaken = System.currentTimeMillis() - startTime;

    loggingRequest(requestWrapper);
    loggingResponse(responseWrapper, request, timeTaken);
    responseWrapper.copyBodyToResponse();
  }

  private void loggingRequest(ContentCachingRequestWrapper request) throws IOException {
    if (log.isDebugEnabled()) {
      var sb = new StringBuilder(LINE_SPARATOR);
      sb.append("------------------------------------- INCOMING Request Begin --------------------------------------")
          .append(LINE_SPARATOR);
      sb.append("PATH        : ").append(request.getRequestURI()).append(LINE_SPARATOR);
      if (StringUtils.isNoneEmpty(request.getQueryString())) {
        sb.append("Query       : ").append(request.getQueryString()).append(LINE_SPARATOR);
      }
      sb.append("Method      : ").append(request.getMethod()).append(LINE_SPARATOR);
      sb.append("Headers     : ").append(headers(request)).append(LINE_SPARATOR);
      sb.append("Body        : ")
          .append(byteToString(request.getContentAsByteArray()))
          .append(LINE_SPARATOR);
      sb.append("-------------------------------------- INCOMING Request End ---------------------------------------");
      log.debug(sb.toString());
    }
  }

  private void loggingResponse(ContentCachingResponseWrapper response, HttpServletRequest request, Long timeTaken)
      throws IOException {
    if (log.isDebugEnabled()) {
      var sb = new StringBuilder(LINE_SPARATOR);
      sb.append("------------------------------------- INCOMING Response Begin --------------------------------------")
          .append(LINE_SPARATOR);
      sb.append("PATH        : ").append(request.getRequestURI()).append(LINE_SPARATOR);
      if (StringUtils.isNoneEmpty(request.getQueryString())) {
        sb.append("Query       : ").append(request.getQueryString()).append(LINE_SPARATOR);
      }
      sb.append("Status      : ").append(response.getStatus()).append(LINE_SPARATOR);
      sb.append("Time        : ").append(StringUtils.join(timeTaken, "ms")).append(LINE_SPARATOR);

      sb.append("Method      : ").append(request.getMethod()).append(LINE_SPARATOR);
      sb.append("Headers     : ").append(headers(response)).append(LINE_SPARATOR);
      sb.append("Body        : ")
          .append(byteToString(response.getContentAsByteArray()))
          .append(LINE_SPARATOR);
      sb.append("-------------------------------------- INCOMING Response End ---------------------------------------");
      log.debug(sb.toString());
    }
  }

  private String headers(ContentCachingResponseWrapper response) {
    var headers = response.getHeaderNames();
    Map<String, Collection<String>> headerMap = new HashedMap<>();
    for (String headerName : headers) {
      headerMap.put(headerName, response.getHeaders(headerName));
    }

    return mapperUtils.writeValueAsString(headerMap);
  }

  private String headers(HttpServletRequest request) {
    var headers = request.getHeaderNames();
    Map<String, Collection<String>> headerMap = new HashedMap<>();
    while (headers.hasMoreElements()) {
      var headerName = headers.nextElement();
      headerMap.put(headerName, convertToStringArray(request.getHeaders(headerName)));
    }

    return mapperUtils.writeValueAsString(headerMap);
  }

  private Collection<String> convertToStringArray(Enumeration<String> enumeration) {
    Collection<String> stringList = new ArrayList<>();
    while (enumeration.hasMoreElements()) {
      stringList.add(enumeration.nextElement());
    }
    return stringList;
  }

  private String byteToString(byte[] value) {
    var object = mapperUtils.readValue(value, Object.class);
    var result = mapperUtils.writeValueAsString(object);

    return ObjectUtils.defaultIfNull(result, StringUtils.EMPTY);
  }

}
