package reisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import reisetech.student.management.data.StudentsCourses;

@Mapper
public interface StudentsCoursesRepository {

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchCourses();

}
