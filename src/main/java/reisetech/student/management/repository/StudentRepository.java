package reisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import reisetech.student.management.data.Student;
import reisetech.student.management.domain.StudentSearchCondition;

/**
 * 受講生テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧(全件)
   */
  List<Student> search();

  /**
   * 検索条件に紐づく受講生を検索します。
   * 受講生コース情報・申込状況とJOINして、コース名や申込状況でも絞り込めるようにしています。
   *
   * @param condition 検索条件
   * @return 検索条件に合致した受講生一覧(重複なし)
   */
  List<Student> searchByCondition(StudentSearchCondition condition);

  /**
   * 受講生の検索を行います。
   *
   * @param id　受講生ID
   * @return 受講生
   */
  Student searchStudent(@Param("id") int id);

  /**
   * 受講生を新規登録します。
   * IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);
}
