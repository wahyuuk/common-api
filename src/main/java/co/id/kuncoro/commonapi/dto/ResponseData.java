package co.id.kuncoro.commonapi.dto;

import static co.id.kuncoro.commonapi.constant.ErrorCode.SUCCESS;

import co.id.kuncoro.commonapi.util.TracerUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {

  private String title;
  private String path;
  private String sourceSystem;

  @Default
  private String code = SUCCESS.getCode();
  private String traceId;
  private String message;
  private T results;
  
  public ResponseData(T results) {
    this.results = results;
  }
  
  public static <T> ResponseData<T> ok(T results, TracerUtils tracerUtils) {
    var response = new ResponseData<>(results);
    response.setTraceId(tracerUtils.getTraceId());
    return response;
  }

}
