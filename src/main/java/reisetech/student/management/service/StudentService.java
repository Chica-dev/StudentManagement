package reisetech.student.management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentsCourses;
import reisetech.student.management.domain.StudentDetail;
import reisetech.student.management.repository.StudentRepository;
import reisetech.student.management.repository.StudentsCoursesRepository;

@Service
public class StudentService {

  final private StudentRepository repository;
  final private StudentsCoursesRepository studentsCoursesRepository;

  @Autowired
  public StudentService(StudentRepository studentrepository,
      StudentsCoursesRepository studentsCoursesRepository) {
    this.repository = studentrepository;
    this.studentsCoursesRepository = studentsCoursesRepository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<Student> searchStudentListByAge(Integer minAge, Integer maxAge) {
    return repository.search()
        .stream()
        .filter(s -> s.getAge() >= minAge && s.getAge() <= maxAge)
        .collect(Collectors.toList());
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return studentsCoursesRepository.searchCourses();
  }

  public List<StudentsCourses> searchStudentsCourseListByCourse() {
    return studentsCoursesRepository.searchCourses()
        .stream()
        .filter(s -> s.getCourse().equals("Java"))
        .collect(Collectors.toList());
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.insertStudent(student);

    for (StudentsCourses courses : studentDetail.getStudentsCourses()) {
      courses.setStudentId((student.getId()));
      LocalDate startDate = LocalDate.now();
      courses.setStartDate(startDate);
      courses.setExpectedEndDate(startDate.plusMonths(6));
      studentsCoursesRepository.insertStudentsCourses(courses);
    }
  }

  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = studentsCoursesRepository.searchCoursesByStudentId(id);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);
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


