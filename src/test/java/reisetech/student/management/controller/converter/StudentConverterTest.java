package reisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentCourse;
import reisetech.student.management.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void setUp() {
    sut = new StudentConverter();
  }

  private Student createStudent(Integer id, String fullName) {
    Student student = new Student();
    student.setId(id);
    student.setFullName(fullName);
    student.setFurigana("フリガナ");
    student.setNickname("ニックネーム");
    student.setEmail("test" + id + "@example.com");
    student.setCity("東京都");
    student.setAge(20);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }

  private StudentCourse createStudentCourse(Integer id, Integer studentId, String course) {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(id);
    studentCourse.setStudentId(studentId);
    studentCourse.setCourse(course);
    studentCourse.setStartDate(LocalDate.now());
    studentCourse.setExpectedEndDate(LocalDate.now().plusYears(1));
    return studentCourse;
  }

  @Test
  void 受講生に紐づく受講生コース情報が正しくマッピングされること() {
    Student student1 = createStudent(1, "山田太郎");
    Student student2 = createStudent(2, "鈴木花子");
    List<Student> studentList = List.of(student1, student2);

    StudentCourse course1 = createStudentCourse(1, 1, "Javaコース");
    StudentCourse course2 = createStudentCourse(2, 1, "AWSコース");
    StudentCourse course3 = createStudentCourse(3, 2, "デザインコース");
    List<StudentCourse> studentCourseList = List.of(course1, course2, course3);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(2);

    StudentDetail detail = actual.get(0);
    assertThat(detail.getStudent()).isEqualTo(student1);
    assertThat(detail.getStudentCourseList()).containsExactly(course1, course2);

    StudentDetail detail2 =actual.get(1);
    assertThat(detail2.getStudent()).isEqualTo(student2);
    assertThat(detail2.getStudentCourseList()).containsExactly(course3);
  }

  @Test
  void 紐づく受講生コース情報が存在しない受講生には空のリストがセットされること() {
    Student student1 = createStudent(1, "山田太郎");
    List<Student> studentList = List.of(student1);

    StudentCourse course1 = createStudentCourse(1, 999, "他の受講生のコース");
    List<StudentCourse> studentCourseList = List.of(course1);

    List<StudentDetail> actual =sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).getStudent()).isEqualTo(student1);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
  }

  @Test
  void 受講生リストが空の場合は空のリストが返ること() {
    List<Student> studentList = List.of();
    StudentCourse course1 = createStudentCourse(1, 1, "Javaコース");
    List<StudentCourse> studentCourseList = List.of(course1);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生コース情報が空の場合は前受講生のコースリストが空になること() {
    Student student1 = createStudent(1, "山田太郎");
    List<Student> studentList = List.of(student1);
    List<StudentCourse> studentCourseList = List.of();

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
  }
}