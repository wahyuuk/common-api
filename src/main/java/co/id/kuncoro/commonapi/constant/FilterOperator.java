package co.id.kuncoro.commonapi.constant;

import java.util.EnumSet;
import java.util.Set;

public enum FilterOperator {

  LIKE,
  START_WITH,
  END_WITH,
  EQUALS,
  NOT_EQUALS,
  GREATER_THAN,
  LESS_THAN,
  GREATER_THAN_OR_EQUALS,
  LESS_THAN_OR_EQUALS,
  IN,
  NOT_IN;

  public static Set<FilterOperator> numberOperators() {
    return EnumSet.of(EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN_OR_EQUALS, IN,
        NOT_IN);
  }

  public static Set<FilterOperator> stringOperators() {
    return EnumSet.of(LIKE, START_WITH, END_WITH, EQUALS, NOT_EQUALS, IN, NOT_IN);
  }

}
