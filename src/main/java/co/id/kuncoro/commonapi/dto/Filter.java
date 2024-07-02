package co.id.kuncoro.commonapi.dto;

import co.id.kuncoro.commonapi.constant.FilterOperator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Filter {

  private String field;
  private Object value;
  private FilterOperator operator;

}
