package co.id.kuncoro.commonapi.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorValidationDto {

  private String field;
  private List<String> message;
  private Object rejectedValue;

}
