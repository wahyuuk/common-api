package co.id.kuncoro.commonapi.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class LanguageUtils {

  private LanguageUtils() {
  }

  public static String getValue(String valueEn, String valueId, HttpServletRequest servletRequest) {
    return getValue(valueEn, valueId, getLocale(servletRequest));
  }

  public static String getValue(String valueEn, String valueId, Locale locale) {
    if (Locale.ENGLISH.equals(locale)) {
      return valueEn;
    }

    return valueId;
  }

  public static Locale getLocale(HttpServletRequest request) {
    var header = request.getHeader("Accept-Language");
    if (StringUtils.isEmpty(header)) {
      return Locale.forLanguageTag("id");
    }

    return Locale.forLanguageTag(header);
  }

}
