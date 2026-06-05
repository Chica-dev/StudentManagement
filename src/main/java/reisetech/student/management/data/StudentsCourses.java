package reisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;


  @JsonPropertyOrder({"id", "studentId", "course", "startDate", "expectedEndDate"})
  @Getter
  @Setter
public class StudentsCourses {

  private Integer id;
  private String studentId;
  private String course;
  private String startDate;
  private String expectedEndDate;
}



