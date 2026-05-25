package reisetech.student.management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentCourseController {

  @Autowired
  private StudentCourseRepository repository;

  @GetMapping("/studentsCourses/detail")
  public StudentCourse searchByCourseId(@RequestParam Integer id) {
    return repository.searchByCourseId(id);
  }

  @GetMapping("/studentsCourses")
  public List<StudentCourse> getCourse(@RequestParam Integer studentId) {
    return repository.searchByStudentId(studentId);
  }

  @GetMapping("/studentsCourses/all")
  public List<StudentCourse> getAllCourses() {
    return repository.searchAllCourses();
  }

  @PostMapping("/studentsCourses")
  public void registerCourse(@RequestBody StudentCourse course) {
    repository.registerCourse(course);
  }

  @PatchMapping("/studentsCourses")
  public void updateCourse(@RequestBody StudentCourse course) {
    repository.updateCourse(course);
  }

  @DeleteMapping("/studentsCourses/byStudent")
  public void deleteCourseByStudentId(@RequestParam Integer studentId) {
    repository.deleteCourseByStudentId(studentId);
  }

  @DeleteMapping("/studentsCourses")
  public void deleteCourse(@RequestParam Integer id) {
    repository.deleteCourse(id);
  }
}
