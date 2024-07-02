package co.id.kuncoro.commonapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorResponseDto<T> extends ResponseData<T> {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<ErrorValidationDto> errors;

}
