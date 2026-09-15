package reisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import reisetech.student.management.data.Student;
import reisetech.student.management.domain.StudentSearchCondition;

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

  @Test
  void 検索条件_氏名の部分一致で検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setFullName("田中");

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getFullName()).isEqualTo("田中一郎");
  }

  @Test
  void 検索条件_フリガナの部分一致で検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setFurigana("ワタナベ");

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(3);
  }

  @Test
  void 検索条件_年齢の範囲で検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setMinAge(30);

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 検索条件_性別で検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setGender("その他");

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getFullName()).isEqualTo("高橋翔");
  }

  @Test
  void 検索条件_地域の部分一致で検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setCity("市");

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 検索条件_コース名で検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setCourse("Python");

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 検索条件_申込状況で検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setStatus("キャンセル");

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 検索条件_コース名と申込状況を組み合わせて検索できること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setCourse("AWS");
    condition.setStatus("受講終了");

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 検索条件_何も指定しない場合は全件返ってくること() {
    StudentSearchCondition condition = new StudentSearchCondition();

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(8);
  }
}