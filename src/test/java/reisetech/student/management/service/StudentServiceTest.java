package reisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reisetech.student.management.controller.converter.StudentConverter;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentCourse;
import reisetech.student.management.domain.StudentDetail;
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
    when(repository.search()).thenReturn(studentList);
    when(studentsCoursesRepository.searchCourse()).thenReturn(studentCourseList);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(studentsCoursesRepository, times(1)).searchCourse();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
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
    StudentCourse course2 = new StudentCourse();
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
    verify(repository, times(1)).updateStudent(student);
    verify(studentsCoursesRepository, times(0)).updateStudentCourse(course);
  }
}