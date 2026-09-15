package reisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import reisetech.student.management.data.ApplicationStatus;

/**
 * 申込状況テーブルと紐づくRepositoryです。
 *
 */

@Mapper
public interface ApplicationStatusRepository {

  /**
   * 申込状況の全件検索を行います。
   * @return 申込状況一覧(全件)
   */
  List<ApplicationStatus> search();

  /**
   * 受講生コース情報IDに紐ずく申込状況を検索します。
   *
   * @param studentsCoursesId 受講生コース情報ID
   * @return 申込状況
   */
  ApplicationStatus searchByStudentsCoursesId(@Param("studentsCoursesId") int studentsCoursesId);

  /**
   * 申込状況を新規登録します。
   *  IDに関して自動採番を行う。
   *
   * @param applicationStatus 申込状況
   */
  void registerApplicationStatus(ApplicationStatus applicationStatus);

  /**
   * 申込状況を更新します。
   * @param applicationStatus 申込状況
   */
  void updateApplicationStatus(ApplicationStatus applicationStatus);
}
