package reisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"id", "fullName", "furigana", "nickname", "email", "city", "age", "gender"})
@Getter
@Setter
public class Student {

  private Integer id;

  @NotBlank
  private String fullName;

  @NotBlank
  private String furigana;

  private String nickname;

  @NotBlank
  @Email
  private String email;

  private String city;

  @Min(0)
  @Max(99)
  private Integer age;

  private String gender;
  private String remark;
  private boolean deleted;
}
