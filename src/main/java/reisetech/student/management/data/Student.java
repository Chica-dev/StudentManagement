package reisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"id", "fullName", "furigana", "nickname", "email", "city", "age", "gender"})
@Getter
@Setter
public class Student {

  private Integer id;
  private String fullName;
  private String furigana;
  private String nickname;
  private String email;
  private String city;
  private Integer age;
  private String gender;
  private String remark;
  private boolean deleted;
}
