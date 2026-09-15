package reisetech.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reisetech.student.management.data.ApplicationStatus;
import reisetech.student.management.exception.InvalidStatusTransitionException;
import reisetech.student.management.repository.ApplicationStatusRepository;

class ApplicationStatusServiceTest {

  private ApplicationStatusService sut;

  @Mock
  private ApplicationStatusRepository repository;

  @BeforeEach
  void before() {
    MockitoAnnotations.openMocks(this);
    sut = new ApplicationStatusService(repository);
  }

  private ApplicationStatus createApplicationStatus(int studentsCoursesId, String staus) {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStudentsCoursesId(studentsCoursesId);
    applicationStatus.setStatus(staus);
    return applicationStatus;
  }

  @Test
  void 申込状況の一覧検索_リポジトリの処理が適切に呼び出せていること() {
    sut.searchApplicationStatusList();

    verify(repository, times(1)).search();
  }

  @Test
  void 受講生コース情報IDに紐づく申込状況の検索_リポジトリの処理が適切に呼び出せていること() {
    sut.searchApplicationStatus(1);

    verify(repository, times(1)).searchByStudentsCoursesId(1);
  }

  @Test
  void 申込状況の新規登録_状態は必ず仮申込で登録されること() {
    ApplicationStatus actual = sut.registerApplicationStatus(1);

    assertThat(actual.getStatus()).isEqualTo("仮申込");
    verify(repository, times(1)).registerApplicationStatus(actual);
  }

  @ParameterizedTest
  @CsvSource({
      "仮申込, 本申込",
      "仮申込, キャンセル",
      "本申込, 受講中",
      "本申込, キャンセル",
      "受講中, 受講終了",
      "受講中, キャンセル",
      "仮申込, 仮申込",
  })
  void 許可された状態遷移の場合は更新が行えること(String currentStatus, String nextStatus) {
    when(repository.searchByStudentsCoursesId(1))
        .thenReturn(createApplicationStatus(1, currentStatus));
    ApplicationStatus updated = createApplicationStatus(1, nextStatus);

    sut.updateApplicationStatus(updated);

    verify(repository, times(1)).updateApplicationStatus(updated);
  }

  @ParameterizedTest
  @CsvSource({
      "仮申込, 受講中",
      "仮申込, 受講終了",
      "本申込, 仮申込",
      "本申込, 受講終了",
      "受講中, 仮申込",
      "受講中, 本申込",
      "受講終了, 仮申込",
      "受講終了, キャンセル",
      "キャンセル, 仮申込",
  })
  void 許可されない状態遷移の場合は例外がスローされること(String currentStatus, String nextStatus) {
    when(repository.searchByStudentsCoursesId(1))
        .thenReturn(createApplicationStatus(1, currentStatus));
    ApplicationStatus updated = createApplicationStatus(1, nextStatus);

    assertThatThrownBy(() -> sut.updateApplicationStatus(updated))
        .isInstanceOf(InvalidStatusTransitionException.class)
        .hasMessage(currentStatus + "から" + nextStatus + "への変更はできません。");
  }
}