package reisetech.student.management.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reisetech.student.management.data.ApplicationStatus;
import reisetech.student.management.exception.InvalidStatusTransitionException;
import reisetech.student.management.repository.ApplicationStatusRepository;

/**
 * 申込状況を取り扱うサービスです。 申込状況の検索・登録・更新を行います。
 *
 */
@Service
public class ApplicationStatusService {

  private static final String STATUS_PROVISIONAL = "仮申込";
  private static final String STATUS_FORMAL = "本申込";
  private static final String STATUS_IN_PROGRESS = "受講中";
  private static final String STATUS_COMPLETED = "受講終了";
  private static final String STATUS_CANCELED = "キャンセル";

  /**
   * 各状態から遷移可能な状態の一覧です。 「１つ先の状態」と「キャンセル」への遷移、および「同じ状態への据え置き」を許可します。 受講終了・キャンセルは終端状態のため、遷移先はありません。
   *
   */
  private static final Map<String, List<String>> ALLOWED_TRANSITIONS = Map.of(
      STATUS_PROVISIONAL, List.of(STATUS_PROVISIONAL, STATUS_FORMAL, STATUS_CANCELED),
      STATUS_FORMAL, List.of(STATUS_FORMAL, STATUS_IN_PROGRESS, STATUS_CANCELED),
      STATUS_IN_PROGRESS, List.of(STATUS_IN_PROGRESS, STATUS_COMPLETED, STATUS_CANCELED),
      STATUS_COMPLETED, List.of(STATUS_COMPLETED),
      STATUS_CANCELED, List.of(STATUS_CANCELED)
  );

  private final ApplicationStatusRepository repository;

  @Autowired
  public ApplicationStatusService(ApplicationStatusRepository repository) {
    this.repository = repository;
  }

  /**
   * 申込状況の一覧検索です。 全件詮索を行うので、条件指定は行いません。
   *
   * @return 申込状況一覧(全件)
   */
  public List<ApplicationStatus> searchApplicationStatusList() {
    return repository.search();
  }

  /**
   * 受講生コース情報IDに紐づく申込状況の検索です。
   *
   * @param studentsCoursesId 受講生コース情報ID
   * @return 申込状況
   */
  public ApplicationStatus searchApplicationStatus(int studentsCoursesId) {
    return repository.searchByStudentsCoursesId(studentsCoursesId);
  }

  /**
   * 申込状況の新規登録を行います。 登録時の状態は必ず「仮申込」で固定します。
   *
   * @param studentsCoursesId 受講生コース情報ID
   * @return 登録した申込状況
   */
  public ApplicationStatus registerApplicationStatus(int studentsCoursesId) {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStudentsCoursesId(studentsCoursesId);
    applicationStatus.setStatus(STATUS_PROVISIONAL);
    repository.registerApplicationStatus(applicationStatus);
    return applicationStatus;
  }

  /**
   * 申込状況の更新を行います。 更新前は状態遷移ルールに沿っているかを検証し、不正な遷移の場合は例外をスローします。
   *
   * @param applicationStatus 更新後の申込状況
   */
  public void updateApplicationStatus(ApplicationStatus applicationStatus) {
    ApplicationStatus current = repository.searchByStudentsCoursesId(
        applicationStatus.getStudentsCoursesId());
    validateStatusTransition(current.getStatus(), applicationStatus.getStatus());
    repository.updateApplicationStatus(applicationStatus);
  }

  /**
   * 状態遷移が許可されたものかを検証します。
   *
   * @param currentStatus 現在の状態
   * @param nextStatus 更新後の状態
   */
  private void validateStatusTransition(String currentStatus, String nextStatus) {
    List<String> allowedNextStatuses = ALLOWED_TRANSITIONS.get(currentStatus);
    if (allowedNextStatuses == null || !allowedNextStatuses.contains(nextStatus)) {
      throw new InvalidStatusTransitionException(
          currentStatus + "から" + nextStatus + "への変更はできません。");
    }
  }
}
