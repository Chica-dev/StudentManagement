package reisetech.student.management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reisetech.student.management.controller.converter.StudentConverter;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentsCourses;
import reisetech.student.management.domain.StudentDetail;
import reisetech.student.management.service.StudentService;

/**
 *受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@RestController
public class StudentController {

  private final StudentService service;


  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   * @return 受講生一覧(全件)
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable int id) {
    return  service.searchStudent(id);
  }

  /**
   * 受講生コース情報検索です。
   * 全件検索を行うので、条件指定は行いません。
   * @return 受講生コース情報一覧(全件)
   */
  @GetMapping("/studentsCourseList")
  public List<StudentsCourses> getStudentsCourseList() {
    return service.studentsCoursesList();
  }

  @PostMapping("registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生コース情報検索です。
   * コースIDに紐づく任意の受講生の情報を取得します。
   * @param id 受講生コースID
   * @return 受講生
   */
  @GetMapping("/updateStudent/{id}")
  public StudentDetail updateStudent(@PathVariable int id){
    return service.searchStudent(id);
  }

  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
