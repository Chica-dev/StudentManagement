package reisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import reisetech.student.management.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  private Student createTestStudent() {
    Student student = new Student();
    student.setFullName("鈴木一郎");
    student.setFurigana("スズキイチロウ");
    student.setNickname("イチロウ");
    student.setEmail("test@example.com");
    student.setCity("東京都");
    student.setAge(20);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 受講生の検索が行えること() {
    Student actual = sut.searchStudent(1);

    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getFullName()).isEqualTo("山田太郎");
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = createTestStudent();

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(9);
  }

  @Test
  void 受講生の更新が行えること() {
    Student student = createTestStudent();
    sut.registerStudent(student);

    student.setFullName("鈴木二郎");
    student.setCity("大阪府");
    student.setAge(21);

    sut.updateStudent(student);

    Student actual = sut.searchStudent(student.getId());

    assertThat(actual.getFullName()).isEqualTo("鈴木二郎");
    assertThat(actual.getCity()).isEqualTo("大阪府");
    assertThat(actual.getAge()).isEqualTo(21);
  }
}