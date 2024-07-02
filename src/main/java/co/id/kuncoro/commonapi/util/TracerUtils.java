package co.id.kuncoro.commonapi.util;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TracerUtils {

  private final Tracer tracer;

  public String getTraceId() {
    return Optional.ofNullable(tracer.currentSpan())
        .map(Span::context)
        .map(TraceContext::traceId)
        .orElse(null);
  }

}
