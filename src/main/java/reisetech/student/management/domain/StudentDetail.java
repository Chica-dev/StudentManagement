package reisetech.student.management.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentsCourses;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentsCourses> studentsCourses;
}
