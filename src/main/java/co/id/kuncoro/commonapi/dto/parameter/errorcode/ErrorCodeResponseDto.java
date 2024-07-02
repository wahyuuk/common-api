package co.id.kuncoro.commonapi.dto.parameter.errorcode;

import co.id.kuncoro.commonapi.dto.DetailResponseDto;
import co.id.kuncoro.commonapi.util.LanguageUtils;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorCodeResponseDto implements DetailResponseDto<Long> {

  private Long id;
  private String detailEn;
  private String detailId;
  private String titleEn;
  private String titleId;
  private String sourceSystem;
  private String code;

  public String getMessage(Locale locale) {
    return LanguageUtils.getValue(detailEn, detailId, locale);
  }

  public String getTitle(Locale locale) {
    return LanguageUtils.getValue(titleEn, titleId, locale);
  }

}
