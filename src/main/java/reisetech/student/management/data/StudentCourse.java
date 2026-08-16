package reisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "受講生コース情報")
@JsonPropertyOrder({"id", "studentId", "course", "startDate", "expectedEndDate"})
  @Getter
  @Setter
public class StudentCourse {

  private Integer id;
  private Integer studentId;

  @NotBlank(message = "コース名は必須です")
  private String course;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @FutureOrPresent(message = "開始日は今日以降の日付を指定してください")
  private LocalDate startDate;

  @DateTimeFormat(pattern =  "yyyy-MM-dd")
  private LocalDate expectedEndDate;
}



