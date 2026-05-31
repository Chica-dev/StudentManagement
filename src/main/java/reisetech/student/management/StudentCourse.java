package reisetech.student.management;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;


  @JsonPropertyOrder({"id", "student_id", "course", "start_date", "expected_end_date"})
  @Getter
  @Setter
public class StudentCourse {

  private Integer id;
  private String studentId;
  private String course;
  private String startDate;
  private String expectedEndDate;
}



