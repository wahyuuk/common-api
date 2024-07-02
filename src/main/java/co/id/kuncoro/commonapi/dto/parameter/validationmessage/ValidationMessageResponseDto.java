package co.id.kuncoro.commonapi.dto.parameter.validationmessage;

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
public class ValidationMessageResponseDto implements DetailResponseDto<Long> {

  private Long id;
  private String code;
  private String messageEn;
  private String messageId;

  public String getMessage(Locale locale) {
    return LanguageUtils.getValue(messageEn, messageId, locale);
  }

}
