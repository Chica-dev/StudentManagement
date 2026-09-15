package reisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import reisetech.student.management.data.ApplicationStatus;
import reisetech.student.management.data.StudentCourse;

@MybatisTest
class ApplicationStatusRepositoryTest {

  @Autowired
  private ApplicationStatusRepository sut;

  @Autowired
  private StudentsCoursesRepository studentsCoursesRepository;

  private StudentCourse registerNewStudentCourse() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1);
    studentCourse.setCourse("Rudy");
    studentCourse.setStartDate(LocalDate.now());
    studentCourse.setExpectedEndDate(LocalDate.now().plusYears(1));
    studentsCoursesRepository.registerStudentCourse(studentCourse);
    return studentCourse;
  }

  @Test
  void 申込状況の全検索が行えること() {
    List<ApplicationStatus> actual = sut.search();

    assertThat(actual.size()).isEqualTo(11);
  }

  @Test
  void 受講生コース情報IDに紐づく申込状況の検索が行えること() {
    ApplicationStatus actual = sut.searchByStudentsCoursesId(1);

    assertThat(actual.getStudentsCoursesId()).isEqualTo(1);
    assertThat(actual.getStatus()).isEqualTo("仮申込");
  }

  @Test
  void 申込状況の登録が行えること() {
    StudentCourse newCourse = registerNewStudentCourse();

    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStudentsCoursesId(newCourse.getId());
    applicationStatus.setStatus("仮申込");

    sut.registerApplicationStatus(applicationStatus);

    ApplicationStatus actual = sut.searchByStudentsCoursesId(newCourse.getId());

    assertThat(actual.getStatus()).isEqualTo("仮申込");
  }

  @Test
  void 申込状況の更新が行えること() {
    StudentCourse newCourse = registerNewStudentCourse();

    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStudentsCoursesId(newCourse.getId());
    applicationStatus.setStatus("仮申込");
    sut.registerApplicationStatus(applicationStatus);

    applicationStatus.setStatus("本申込");
    sut.updateApplicationStatus(applicationStatus);

    ApplicationStatus actual = sut.searchByStudentsCoursesId(newCourse.getId());

    assertThat(actual.getStatus()).isEqualTo("本申込");
  }
}