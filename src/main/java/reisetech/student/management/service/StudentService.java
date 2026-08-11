package reisetech.student.management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reisetech.student.management.controller.converter.StudentConverter;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentsCourses;
import reisetech.student.management.domain.StudentDetail;
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
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentsCourses> studentsCoursesList = studentsCoursesRepository.searchCourses();
    return converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = studentsCoursesRepository.searchCoursesByStudentId(id);
    return new StudentDetail(student, studentsCourses);
  }

  public List<StudentsCourses> studentsCoursesList() {
    return studentsCoursesRepository.searchCourses();
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.insertStudent(student);
    for (StudentsCourses courses : studentDetail.getStudentsCourses()) {
      courses.setStudentId((student.getId()));
      LocalDate startDate = LocalDate.now();
      courses.setStartDate(startDate);
      courses.setExpectedEndDate(startDate.plusYears(1));
      studentsCoursesRepository.insertStudentsCourses(courses);
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);

    for (StudentsCourses courses : studentDetail.getStudentsCourses()) {
      if (courses.getCourse() == null || courses.getCourse().isBlank()) {
        continue;
      }

      if (courses.getId() == null || courses.getId() == 0) {
        courses.setStudentId(student.getId());
        if (courses.getStartDate() == null) {
          courses.setStartDate(LocalDate.now());
        }
        if (courses.getExpectedEndDate() == null) {
          courses.setExpectedEndDate(courses.getStartDate().plusMonths(6));
        }
        studentsCoursesRepository.insertStudentsCourses(courses);
      } else {

        studentsCoursesRepository.updateStudentsCourses(courses);
      }
    }
  }

}


