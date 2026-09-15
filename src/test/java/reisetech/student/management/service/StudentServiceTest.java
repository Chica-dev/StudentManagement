package reisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reisetech.student.management.controller.converter.StudentConverter;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentCourse;
import reisetech.student.management.domain.StudentDetail;
import reisetech.student.management.domain.StudentSearchCondition;
import reisetech.student.management.exception.InvalidCourseDateRangeException;
import reisetech.student.management.exception.StudentNotFoundException;
import reisetech.student.management.repository.StudentRepository;
import reisetech.student.management.repository.StudentsCoursesRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentsCoursesRepository studentsCoursesRepository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, studentsCoursesRepository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList =new ArrayList<>();

    Student student = new Student();
    student.setId(1);
    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>());
    List<StudentDetail> expected = new ArrayList<>();
    expected.add(studentDetail);

    when(repository.search()).thenReturn(studentList);
    when(studentsCoursesRepository.searchCourse()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(studentsCoursesRepository, times(1)).searchCourse();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
    assertEquals(expected, actual);
  }

  @Test
  void 受講生詳細検索_IDに紐づく受講生が存在する場合に正しく返却されること() {
    int id =1;
    Student student =new Student();
    student.setId(id);
    List<StudentCourse> studentCourseList =new ArrayList<>();

    when(repository.searchStudent(id)).thenReturn(student);
    when(studentsCoursesRepository.searchCourseByStudentId(id)).thenReturn(studentCourseList);

    StudentDetail actual =sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(studentsCoursesRepository, times(1)).searchCourseByStudentId(id);
    assertEquals(student, actual.getStudent());
    assertEquals(studentCourseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生詳細検索_ID紐づく受講生が存在しない場合に例外がスローされること() {
    int id =999;
    when(repository.searchStudent(id)).thenReturn(null);

    StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
        () -> sut.searchStudent(id));

    assertEquals("指定されたID(" + id + ")の受講生が見つかりません。", exception.getMessage());
    verify(studentsCoursesRepository, times(0)).searchCourseByStudentId(id);
  }

  @Test
  void 受講生コース一覧検索_リポジトリの処理が適切に呼び出せていること() {
    List<StudentCourse> studentCourseList =new ArrayList<>();

    when(studentsCoursesRepository.searchCourse()).thenReturn(studentCourseList);

    List<StudentCourse> actual = sut.studentCourseList();

    verify(studentsCoursesRepository, times(1)).searchCourse();
    assertEquals(studentCourseList, actual);
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    student.setId(1);

    StudentCourse course1 = new StudentCourse();
    course1.setId(1);

    StudentCourse course2 = new StudentCourse();
    course2.setId(2);

    List<StudentCourse> studentCourseList = new  ArrayList<>();
    studentCourseList.add(course1);
    studentCourseList.add(course2);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    StudentDetail actual = sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(studentsCoursesRepository, times(1)).registerStudentCourse(course1);
    verify(studentsCoursesRepository, times(1)).registerStudentCourse(course2);
    assertEquals(studentDetail, actual);
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    student.setId(1);

    StudentCourse course = new StudentCourse();
    course.setStartDate(LocalDate.of(2025, 1, 1));
    course.setExpectedEndDate(LocalDate.of(2025, 12, 31));

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(course);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(studentsCoursesRepository, times(1)).updateStudentCourse(course);
  }

  @Test
  void 受講生詳細の更新_複数のコース情報が一括で更新されること() {
    Student student = new Student();
    student.setId(1);

    StudentCourse course1 = new StudentCourse();
    course1.setId(1);
    course1.setStartDate(LocalDate.of(2025, 1, 1));
    course1.setExpectedEndDate(LocalDate.of(2025, 12, 31));

    StudentCourse course2 = new StudentCourse();
    course2.setId(2);
    course2.setStartDate(LocalDate.of(2025, 4, 1));
    course2.setExpectedEndDate(LocalDate.of(2026, 3, 31));

    StudentCourse course3 = new StudentCourse();
    course3.setId(3);
    course3.setStartDate(LocalDate.of(2025, 6, 1));
    course3.setExpectedEndDate(LocalDate.of(2026, 5, 31));

    List<StudentCourse> studentCourseList = List.of(course1, course2, course3);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(studentsCoursesRepository, times(1)).updateStudentCourse(course1);
    verify(studentsCoursesRepository, times(1)).updateStudentCourse(course2);
    verify(studentsCoursesRepository, times(1)).updateStudentCourse(course3);
    verify(studentsCoursesRepository, times(3)).updateStudentCourse(any(StudentCourse.class));
  }

  @Test
  void 受講生詳細の更新_終了予定日が開始日より前の場合に例外がスローされること() {
    Student student = new Student();
    student.setId(1);

    StudentCourse course = new StudentCourse();
    course.setStartDate(LocalDate.of(2025, 12, 31));
    course.setExpectedEndDate(LocalDate.of(2025, 1,1));

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(course);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    InvalidCourseDateRangeException exception =assertThrows(
        InvalidCourseDateRangeException.class,
        () -> sut.updateStudent(studentDetail));

    assertEquals("終了予定日は開始日より後の日付を指定してください。", exception.getMessage());
    verify(repository, times(0)).updateStudent(student);
    verify(studentsCoursesRepository, times(0)).updateStudentCourse(course);
  }

  @Test
  void 受講生詳細の一覧検索_検索受験が空の場合は全件検索と同様の結果が返ってくること() {
    StudentSearchCondition condition = new StudentSearchCondition();

    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentDetail> expected = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(studentsCoursesRepository.searchCourse()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

    List<StudentDetail> actual = sut.searchStudentList(condition);

    verify(repository, times(1)).search();
    verify(repository, times(0)).searchByCondition(condition);
    assertEquals(expected, actual);
  }

  @Test
  void 受講生詳細の一覧検索_検索条件が指定された場合は条件詮索の結果が返ってくること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setFullName("田中");

    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentDetail> expected = new ArrayList<>();

    when(repository.searchByCondition(condition)).thenReturn(studentList);
    when(studentsCoursesRepository.searchCourse()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

    List<StudentDetail> actual = sut.searchStudentList(condition);

    verify(repository, times(1)).searchByCondition(condition);
    verify(repository, times(0)).search();
    assertEquals(expected, actual);
  }
}