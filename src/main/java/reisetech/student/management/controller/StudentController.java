package reisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentsCourses;
import reisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return service.searchStudentList();
  }

  @GetMapping("/studentListByAge")
  public List<Student> getStudentListByAge() {

    return service.searchStudentListByAge(30, 39);
  }

  @GetMapping("/studentsCourseList")
  public List<StudentsCourses> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }

  @GetMapping("/studentsCourseListByCourse")
  public List<StudentsCourses> getStudentsCourseListByCourse() {
    return service.searchStudentsCourseListByCourse();
  }
}
