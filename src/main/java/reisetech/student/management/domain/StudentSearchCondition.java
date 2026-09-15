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
    return fullName == null && furigana == null && nickname == null
        && minAge == null && maxAge == null && gender == null
        && city == null && course == null && status == null;
  }
}
