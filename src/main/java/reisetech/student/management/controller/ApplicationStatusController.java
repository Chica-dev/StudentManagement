package reisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reisetech.student.management.data.ApplicationStatus;
import reisetech.student.management.service.ApplicationStatusService;

/**
 * 申込状況の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 *
 */
@Tag(name = "ApplicationStatus", description = "申込状況に関する操作")
@RestController
public class ApplicationStatusController {

  private final ApplicationStatusService service;

  @Autowired
  public ApplicationStatusController(ApplicationStatusService service) {
    this.service = service;
  }


  /**
   * 申込状況の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 申込状況一覧(全件)
   */
  @Operation(summary = "一覧検索", description = "申込状況の一覧を検索します。"
      + "全件検索を行うので条件指定はおこにません。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "申込状況一覧(全件)の取得に成功",
          content = @Content(schema = @Schema(implementation = ApplicationStatus.class)))
  })
  @GetMapping("/applicationStatusList")
  public List<ApplicationStatus> getApplicationStatusList() {
    return service.searchApplicationStatusList();
  }

  /**
   * 受講生コース情報IDに紐づく申込状況の検索です。
   *
   * @param studentsCoursesId 受講生コース情報ID
   * @return 申込状況
   */
  @Operation(summary = "申込状況の検索", description = "受講生コース情報IDに紐づく申込状況を取得します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "申込状況の取得に成功",
          content = @Content(schema = @Schema(implementation = ApplicationStatus.class)))
  })
  @GetMapping("/applicationStatus")
  public ApplicationStatus getApplicationStatus(
      @Parameter(description = "受講生コース情報ID", example = "1")
      @RequestParam int studentsCoursesId) {
    return service.searchApplicationStatus(studentsCoursesId);
  }

  /**
   * 申込状況の新規登録を行います。
   * 状態は必ず「仮申込」で登録されます。
   *
   * @param studentsCoursesId 受講生コース情報ID
   * @return 登録した申込状況
   */
  @Operation(summary = "申込状況の登録", description = "受講生コース情報に紐づく申込状況を新規登録します。"
  + "状態は必ず「仮申込」で登録されます。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "登録に成功し、登録された申込状況を返却",
      content = @Content(schema = @Schema(implementation = ApplicationStatus.class)))
  })
  @PostMapping("/registerApplicationStatus")
  public ResponseEntity<ApplicationStatus> registerApplicationStatus(
      @Parameter(description = "受講生コース情報ID", example = "1")
      @RequestParam int studentsCoursesId) {
    ApplicationStatus applicationStatus = service.registerApplicationStatus(studentsCoursesId);
    return ResponseEntity.ok(applicationStatus);
  }

  /**
   * 申込状況の更新を行います。
   * 状態遷移ルールに沿わない更新の場合はエラーになります。
   *
   * @param applicationStatus 更新後の申込状況
   * @return 実行結果
   */
  @Operation(summary = "申込状況の更新", description = "申込状況を更新します。"
  + "状態遷移ルールに沿わない更新の場合はエラーになります。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新処理が成功",
      content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "バリデーションエラーまたは不正な状態遷移")
  })
  @PutMapping("/updateApplicationStatus")
  public ResponseEntity<String> updateApplicationStatus(
      @Validated @RequestBody ApplicationStatus applicationStatus) {
    service.updateApplicationStatus(applicationStatus);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
