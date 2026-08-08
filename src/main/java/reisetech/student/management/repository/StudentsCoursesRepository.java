package reisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import reisetech.student.management.data.StudentsCourses;

@Mapper
public interface StudentsCoursesRepository {

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchCourses();

    @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
    List<StudentsCourses> searchCoursesByStudentId(int studentId);

    @Insert("INSERT INTO students_courses(student_id, course, start_date, expected_end_date)" +
        "VALUES (#{studentId}, #{course}, #{startDate}, #{expectedEndDate})")
    @Options (useGeneratedKeys = true, keyProperty = "id")
    void insertStudentsCourses(StudentsCourses studentsCourses);

    @Update("UPDATE students_courses SET course = #{course}, start_date = #{startDate}, "
        + "expected_end_date = #{expectedEndDate} WHERE id = #{id}")
    void updateStudentsCourses(StudentsCourses studentsCourses);
}
