package reisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reisetech.student.management.data.StudentCourse;
import reisetech.student.management.domain.StudentDetail;
import reisetech.student.management.domain.StudentSearchCondition;
import reisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Tag(name = "Student", description = "受講生に関する操作")
@RestController
public class StudentController {

  private final StudentService service;


  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。
   * 検索条件を指定しない場合は全件検索、指定した場合はその条件に合致する受講生を検索します。
   * @param fullName 氏名(部分一致)
   * @param furigana フリガナ(部分一致)
   * @param nickname ニックネーム(部分一致)
   * @param minAge 年齢(下限)
   * @param maxAge 年齢(上限)
   * @param gender 性別
   * @param city 地域(部分一致)
   * @param course コース名(部分一致)
   * @param status 申込状況
   * @return 受講生詳細一覧
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。"
      + "検索条件を指定しない場合は全件検索、指定した場合はその条件に合致する受講生を検索します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "受講生詳細一覧(全件)の取得に成功",
          content = @Content(schema = @Schema(implementation = StudentDetail.class)))
  })
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList(
      @Parameter(description = "氏名(部分一致)") @RequestParam(required = false) String fullName,
      @Parameter(description = "フリガナ(部分一致)") @RequestParam(required = false) String furigana,
      @Parameter(description = "ニックネーム(部分一致)") @RequestParam(required = false) String nickname,
      @Parameter(description = "年齢(下限)") @RequestParam(required = false) Integer minAge,
      @Parameter(description = "年齢(上限)") @RequestParam(required = false) Integer maxAge,
      @Parameter(description = "性別") @RequestParam(required = false) String gender,
      @Parameter(description = "地域(部分一致)") @RequestParam(required = false) String city,
      @Parameter(description = "コース名(部分一致)") @RequestParam(required = false) String course,
      @Parameter(description = "申込状況") @RequestParam(required = false) String status) {

    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setFullName(fullName);
    condition.setFurigana(furigana);
    condition.setNickname(nickname);
    condition.setMinAge(minAge);
    condition.setMaxAge(maxAge);
    condition.setGender(gender);
    condition.setCity(city);
    condition.setCourse(course);
    condition.setStatus(status);

    if (condition.isEmpty()) {
      return service.searchStudentList();
    }
    return service.searchStudentList(condition);
  }

  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(summary = "受講生詳細の検索", description = "IDに紐づく任意の受講生の情報を取得します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "受講生詳細の取得に成功",
          content = @Content(schema = @Schema(implementation = StudentDetail.class))),
      @ApiResponse(responseCode = "404", description = "指定されたIDの受講生が存在しない")
  })
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @Parameter(description = "受講生ID", example = "1") @PathVariable int id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生コース情報検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生コース情報一覧(全件)
   */
  @Operation(summary = "受講生コース情報検索", description = "受講生コース情報の一覧を検索します。"
      + "全件検索を行うので、条件指定は行いません。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "受講生コース情報一覧(全件)の取得に成功",
          content = @Content(schema = @Schema(implementation = StudentCourse.class)))
  })
  @GetMapping("/studentsCourseList")
  public List<StudentCourse> getStudentsCourseList() {
    return service.studentCourseList();
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "登録に成功し、登録された受講生詳細を返却",
          content = @Content(schema = @Schema(implementation = StudentDetail.class))),
      @ApiResponse(responseCode = "400", description = "バリデーションエラー")
  })
  @PostMapping("registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @Validated @RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細のコース情報検索です。 コースIDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生詳細のコースID
   * @return 受講生詳細
   */
  @Operation(summary = "更新用の受講生詳細取得", description = "コースIDに紐づく任意の受講生の情報を取得します。"
      + "更新画面の初期表示です。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "受講生詳細の取得に成功",
          content = @Content(schema = @Schema(implementation = StudentDetail.class))),
      @ApiResponse(responseCode = "404", description = "指定されたIDの受講生が存在しない")
  })
  @GetMapping("/updateStudent/{id}")
  public StudentDetail updateStudent(
      @Parameter(description = "受講生詳細のコースID", example = "1") @PathVariable int id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の更新を行います。 削除フラグの更新もここで行います(論理削除)
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生更新", description = "受講生詳細の更新を行います。"
      + "削除フラグ(論理削除)の更新もここで行います。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新処理が成功",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "バリデーションエラー")
  })
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@Validated @RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
