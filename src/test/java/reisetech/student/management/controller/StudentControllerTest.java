package reisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentCourse;
import reisetech.student.management.domain.StudentDetail;
import reisetech.student.management.exception.StudentNotFoundException;
import reisetech.student.management.service.StudentService;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JsonMapper jsonMapper;

  @MockitoBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Student createValidStudent() {
    Student student = new Student();
    student.setFullName("江並公史");
    student.setFurigana("エナミコウジ");
    student.setEmail("test@example.com");
    return student;
  }

  private StudentCourse createValidStudentCourse() {
    StudentCourse course = new StudentCourse();
    course.setCourse("Javaコース");
    course.setStartDate(LocalDate.now());
    course.setExpectedEndDate(LocalDate.now().plusYears(1));
    return course;
  }

  private StudentDetail createValidStudentDetail() {
    return new StudentDetail(createValidStudent(), List.of(createValidStudentCourse()));
  }

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    List<StudentDetail> emptyList = List.of();
    when(service.searchStudentList()).thenReturn(emptyList);

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の一覧検索が実行できていて受講生詳細のリストが返ってくること() throws Exception {
    StudentDetail studentDetail = createValidStudentDetail();
    studentDetail.getStudent().setId(1);

    when(service.searchStudentList()).thenReturn(List.of(studentDetail));

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].student.fullName").value("江並公史"))
        .andExpect(jsonPath("$[0].studentCourseList[0].course").value("Javaコース"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = createValidStudent();

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生で名前が空白を用いた時に入力チェックに掛かること() {
    Student student = createValidStudent();
    student.setFullName("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("氏名は必須です");
  }

  @Test
  void 受講生詳細の受講生が単一検索で正常に取得できること() throws Exception {
    Student student = createValidStudent();
    student.setId(1);

    StudentDetail studentDetail = new StudentDetail(student, List.of());

    when(service.searchStudent(1)).thenReturn(studentDetail);

    mockMvc.perform(get("/student/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.fullName").value("江並公史"));

    verify(service, times(1)).searchStudent(1);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() throws Exception {
    mockMvc.perform(get("/student/{id}", "abc"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).searchStudent(anyInt());
  }

  @Test
  void 受講生詳細の受講生で存在しないIDを指定した時に404が返ること() throws Exception {
    when(service.searchStudent(999)).thenThrow(
        new StudentNotFoundException("受講生がに見つかりません"));

    mockMvc.perform(get("/student/{id}", 999))
        .andExpect(status().isNotFound());

    verify(service, times(1)).searchStudent(999);
  }

  @Test
  void 受講生詳細の登録が正常に実行できること() throws Exception {
    StudentDetail requestDetail = createValidStudentDetail();

    Student savedStudent = createValidStudent();
    savedStudent.setId(1);
    StudentDetail responseDetail = new StudentDetail(savedStudent, requestDetail.getStudentCourseList());

    when(service.registerStudent(any(StudentDetail.class))).thenReturn(responseDetail);

    mockMvc.perform(post("/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(requestDetail)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value(1));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細の登録で氏名が未入力の時に入力チェックに掛かること() throws Exception {
    Student student = createValidStudent();
    student.setFullName("");
    StudentDetail requestDetail = new StudentDetail(student,List.of(createValidStudentCourse()));

    mockMvc.perform(post("/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(requestDetail)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['student.fullName']").value("氏名は必須です"));

    verify(service, times(0)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生コース情報の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    when(service.studentCourseList()).thenReturn(List.of());

    mockMvc.perform(get("/studentsCourseList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).studentCourseList();
  }

  @Test
  void 受講生コース情報の一覧検索が実行できてコース情報のリストが返ってくること() throws  Exception {
    StudentCourse course =createValidStudentCourse();
    course.setId(1);
    course.setStudentId(1);

    when(service.studentCourseList()).thenReturn(List.of(course));

    mockMvc.perform(get("/studentsCourseList"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].course").value("Javaコース"));

    verify(service, times(1)).studentCourseList();
  }

  @Test
  void 受講生詳細の更新用取得が正常に実行できること() throws  Exception {
    Student student = createValidStudent();
    student.setId(1);
    StudentDetail studentDetail = new StudentDetail(student, List.of());

    when(service.searchStudent(1)).thenReturn(studentDetail);

    mockMvc.perform(get("/updateStudent/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.fullName").value("江並公史"));

    verify(service, times(1)).searchStudent(1);
  }

  @Test
  void 受講生詳細の更新用取得でIDに数字以外を用いた時に入力チェックに掛かること() throws Exception {
    mockMvc.perform(get("/updateStudent/{id}", "abc"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).searchStudent(anyInt());
  }

  @Test
  void 受講生詳細の更新用取得で存在しないIDを指定した時に404が返ること() throws  Exception {
    when(service.searchStudent(999)).thenThrow(new StudentNotFoundException("受講生が見つかりません"));

    mockMvc.perform(get("/updateStudent/{id}", 999))
        .andExpect(status().isNotFound());

    verify(service, times(1)).searchStudent(999);
  }

  @Test
  void 受講生詳細の更新が正常に実行できること() throws Exception {
    StudentDetail requestDetail = createValidStudentDetail();
    requestDetail.getStudent().setId(1);

    mockMvc.perform(put("/updateStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(requestDetail)))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));
  }

  @Test
  void 受講生詳細の更新で氏名が未入力の時に入力チェックに掛かること() throws Exception {
    Student student = createValidStudent();
    student.setId(1);
    student.setFullName("");
    StudentDetail requesDetail = new StudentDetail(student, List.of(createValidStudentCourse()));

    mockMvc.perform(put("/updateStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(requesDetail)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['student.fullName']").value("氏名は必須です"));

    verify(service, times(0)).updateStudent(any(StudentDetail.class));
  }
}