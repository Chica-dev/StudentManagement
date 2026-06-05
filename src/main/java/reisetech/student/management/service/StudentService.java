package reisetech.student.management.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reisetech.student.management.data.Student;
import reisetech.student.management.data.StudentsCourses;
import reisetech.student.management.repository.StudentRepository;
import reisetech.student.management.repository.StudentsCoursesRepository;

@Service
public class StudentService {

  final private StudentRepository repository;
  final private StudentsCoursesRepository studentsCoursesRepository;

  @Autowired
  public StudentService(StudentRepository repository,
      StudentsCoursesRepository studentsCoursesRepository) {
    this.repository = repository;
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
}


