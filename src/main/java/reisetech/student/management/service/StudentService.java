package reisetech.student.management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reisetech.student.management.controller.converter.StudentConverter;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentCourse;
import reisetech.student.management.domain.StudentDetail;
import reisetech.student.management.exception.InvalidCourseDateRangeException;
import reisetech.student.management.exception.StudentNotFoundException;
import reisetech.student.management.repository.StudentRepository;
import reisetech.student.management.repository.StudentsCoursesRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索・更新情報をおこないます。
 */
@Service
public class StudentService {

  final private StudentRepository repository;
  final private StudentsCoursesRepository studentsCoursesRepository;
  private final StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository studentrepository,
      StudentsCoursesRepository studentsCoursesRepository, StudentConverter converter) {
    this.repository = studentrepository;
    this.studentsCoursesRepository = studentsCoursesRepository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = studentsCoursesRepository.searchCourse();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudent(id);
    if (student == null) {
      throw  new StudentNotFoundException("指定されたID(" + id + ")の受講生が見つかりません。");
    }
    List<StudentCourse> studentCourse = studentsCoursesRepository.searchCourseByStudentId(id);
    return new StudentDetail(student, studentCourse);
  }
  public List<StudentCourse> studentCourseList() {
    return studentsCoursesRepository.searchCourse();
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値やコース開始日、コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    LocalDate startDate = LocalDate.now();
    LocalDate expectedEndDate = startDate.plusYears(1);

    repository.registerStudent(student);
    studentDetail.getStudentCourseList().forEach(courses -> {
      initStudentCourse(courses, student, startDate, expectedEndDate);
      studentsCoursesRepository.registerStudentCourse(courses);
    });
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param courses 受講生コース情報
   * @param student 受講生
   * @param startDate コース開始日
   * @param expectedEndDate コース終了予定日
   */
  private void initStudentCourse(StudentCourse courses, Student student, LocalDate startDate,
      LocalDate expectedEndDate) {
    courses.setStudentId((student.getId()));
    courses.setStartDate(startDate);
    courses.setExpectedEndDate(expectedEndDate);
  }

  /**
   * 受講生詳細の更新を行います。
   * 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    studentDetail.getStudentCourseList().forEach(this::validateCourseDateRange);

    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList().forEach(studentsCoursesRepository::updateStudentCourse);
  }

  private void validateCourseDateRange(StudentCourse course) {
    if (course.getStartDate() != null && course.getExpectedEndDate() != null
    && course.getExpectedEndDate().isBefore(course.getStartDate())) {
      throw new InvalidCourseDateRangeException("終了予定日は開始日より後の日付を指定してください。");
    }
  }
}


