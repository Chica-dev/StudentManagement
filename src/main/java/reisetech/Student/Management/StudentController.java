package reisetech.Student.Management;

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
public class StudentController {

  @Autowired
  private StudentRepository repository;

  @GetMapping("/students")
  public Student getStudent(@RequestParam String fullName) {
    Student student = repository.searchByName(fullName);
    return student;
  }

  @GetMapping("/students/all")
  public List<Student> getAllStudents() {
    return repository.searchAllStudents();
  }

  @PostMapping("/students")
  public void registerStudent(@RequestBody Student student) {
    repository.registerStudent(student);
  }

  @PatchMapping("/students")
  public void updateStudent(@RequestBody Student student) {
    repository.updateStudent(student);
  }

  @DeleteMapping("/students")
  public void deleteStudent(@RequestParam Integer id) {
    repository.deleteStudent(id);
  }

  @DeleteMapping("/students/byName")
  public void deleteStudentByName(@RequestParam String fullName) {
    repository.deleteStudentByName(fullName);
  }
}
