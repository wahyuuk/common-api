package co.id.kuncoro.commonapi.dto;

import static co.id.kuncoro.commonapi.constant.Constants.COMMA;
import static co.id.kuncoro.commonapi.constant.Constants.DEFAULT_MAX_PAGE_SIZE;
import static co.id.kuncoro.commonapi.constant.Constants.DEFAULT_PAGE_NUMBER;
import static co.id.kuncoro.commonapi.constant.Constants.DEFAULT_PAGE_SIZE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamsRequest {

  @Default
  private Integer pageSize = DEFAULT_PAGE_SIZE;

  @Default
  private Integer pageNumber = DEFAULT_PAGE_NUMBER;

  private String sort;

  public Integer getPageSize() {
    if (pageSize == null) {
      return DEFAULT_PAGE_SIZE;
    }

    if (pageSize > DEFAULT_MAX_PAGE_SIZE) {
      return DEFAULT_MAX_PAGE_SIZE;
    }

    return Math.max(INTEGER_ONE, pageSize);
  }

  public Integer getPageNumber() {
    return Math.max(pageNumber, DEFAULT_PAGE_NUMBER) - 1;
  }

  public Sort getSort() {
    if (StringUtils.isEmpty(sort)) {
      return Sort.unsorted();
    }

    Set<String> propSet = new HashSet<>();
    var orders = Arrays.stream(StringUtils.split(sort, COMMA))
        .map(this::createOrder)
        .filter(e -> {
          if (propSet.contains(e.getProperty())) {
            return false;
          }

          propSet.add(e.getProperty());
          return true;
        })
        .toList();

    return Sort.by(orders);
  }

  public PageRequest getPageRequest() {
    return PageRequest.of(getPageNumber(), getPageSize(), getSort());
  }

  private Order createOrder(String sort) {
    var sortArr = StringUtils.split(sort);
    var direction = Direction.ASC;
    if (sortArr.length > 1) {
      direction = EnumUtils.getEnumIgnoreCase(Direction.class, sortArr[1], Direction.ASC);
    }

    return new Order(direction, sortArr[0]);
  }

}
