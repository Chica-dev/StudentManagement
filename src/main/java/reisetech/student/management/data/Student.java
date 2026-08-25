package reisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@JsonPropertyOrder({"id", "fullName", "furigana", "nickname", "email", "city", "age", "gender"})
@Getter
@Setter
@EqualsAndHashCode
public class Student {

  private Integer id;

  @NotBlank(message = "氏名は必須です")
  private String fullName;

  @NotBlank(message = "フリガナは必須です")
  private String furigana;

  private String nickname;

  @NotBlank(message = "メールアドレスは必須です")
  @Email(message = "メールアドレスの形式が正しくありません")
  private String email;

  private String city;

  @Min(value = 0, message = "年齢は0以上で入力してください")
  @Max(value = 99, message = "年齢は99以下で入力してください")
  private Integer age;

  private String gender;
  private String remark;
  private boolean deleted;
}
