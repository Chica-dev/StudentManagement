package reisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生検索条件")
@Getter
@Setter
public class StudentSearchCondition {

  private String fullName;

  private String furigana;

  private String nickname;

  private Integer minAge;

  private Integer maxAge;

  private String gender;

  private String city;

  private String course;

  private String status;

  /**
   * すべての検索条件が未設定(null)かどうかを判定します。
   *
   * @return すべて未設定の場合はtrue
   */
  public boolean isEmpty() {
    return isBlank(fullName) && isBlank(furigana) && isBlank(nickname)
        && minAge == null && maxAge == null && isBlank(gender)
        && isBlank(city) && isBlank(course) && isBlank(status);
  }

  /**
   * 文字列がnullまたは空白のみかどうかを判定します。
   *
   * @param value 判定対象の文字列
   * @return nullまたは空白のみの場合はtrue
   */
  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
