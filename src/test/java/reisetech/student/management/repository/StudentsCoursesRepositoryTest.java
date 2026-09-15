package reisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import reisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentsCoursesRepositoryTest {

  @Autowired
  private StudentsCoursesRepository sut;

  private StudentCourse createTestStudentCourse() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1);
    studentCourse.setCourse("Rudy");
    studentCourse.setStartDate(LocalDate.now());
    studentCourse.setExpectedEndDate(LocalDate.now().plusYears(1));
    return studentCourse;
  }

  @Test
  void 受講生コース情報の全検索が行えること() {
    List<StudentCourse> actual = sut.searchCourse();

    assertThat(actual.size()).isEqualTo(11);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    List<StudentCourse> actual = sut.searchCourseByStudentId(1);

    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = createTestStudentCourse();

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchCourse();

    assertThat(actual.size()).isEqualTo(12);
  }

  @Test
  void 受講生コース情報の更新が行えること() {
    StudentCourse studentCourse = createTestStudentCourse();
    sut.registerStudentCourse(studentCourse);

    studentCourse.setCourse("AI開発");

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchCourseByStudentId(1);

    StudentCourse updated = actual.stream()
        .filter(c -> c.getId().equals(studentCourse.getId()))
        .findFirst()
        .orElseThrow();

    assertThat(updated.getCourse()).isEqualTo("AI開発");
  }

  @Test
  void 受講生コース情報IDに紐づく受講生コース情報の検索が行えること() {
    StudentCourse actual = sut.searchCourseById(1);

    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getCourse()).isEqualTo("Java応用");
  }

  @Test
  void 存在しない受講生コース情報IDを指定した場合はnullが返ってくること() {
    StudentCourse actual = sut.searchCourseById(999);

    assertThat(actual).isNull();
  }
}