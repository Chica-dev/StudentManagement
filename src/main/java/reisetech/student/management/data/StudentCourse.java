package reisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@JsonPropertyOrder({"id", "studentId", "course", "startDate", "expectedEndDate"})
  @Getter
  @Setter
public class StudentCourse {

  private Integer id;
  private Integer studentId;
  private String course;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;
  @DateTimeFormat(pattern =  "yyyy-MM-dd")
  private LocalDate expectedEndDate;


}



