package reisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "申込状況")
@JsonPropertyOrder({"id", "studentsCoursesId", "status"})
@Getter
@Setter
@EqualsAndHashCode
public class ApplicationStatus {

  private Integer id;

  private Integer studentsCoursesId;

  @NotBlank(message = "申込状況は必須です")
  private String status;
}
