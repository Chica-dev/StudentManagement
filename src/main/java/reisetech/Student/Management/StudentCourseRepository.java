package reisetech.Student.Management;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentCourseRepository {

  @Select("""
      SELECT * FROM students_courses WHERE id =#{id}
      """)
  StudentCourse searchByCourseId(Integer id);

  @Select("""
      SELECT * FROM students_courses WHERE student_id =#{studentId}
      """)
  List<StudentCourse> searchByStudentId(Integer studentId);

  @Select(""" 
      SELECT * FROM students_courses
      """)
  List<StudentCourse> searchAllCourses();

  @Insert("""
      INSERT INTO students_courses (student_id, course, start_date, expected_end_date)
      VALUES
      (#{studentId}, #{course}, #{startDate}, #{expectedEndDate})
      """)
  void registerCourse(StudentCourse Course);

  @Update("""
      <script>
      UPDATE students_courses
      <set>
        <if test="course != null">course = #{course},</if>
        <if test="startDate != null">start_date = #{startDate},</if>
        <if test="expectedEndDate != null">expected_end_date = #{expectedEndDate},</if>
      </set>
      WHERE id = #{id}
      </script>
      """)
  void updateCourse(StudentCourse course);

  @Delete("""
      DELETE FROM students_courses WHERE id = #{id}
      """)
  void deleteCourse(Integer id);

  @Delete("""
      DELETE FROM students_courses WHERE student_id = #{studentId}
      """)
  void deleteCourseByStudentId(Integer studentId);

}



