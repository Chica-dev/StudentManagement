package reisetech.Student.Management;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
//import org.apache.ibatis.annotations.Delete.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select(""" 
      SELECT * FROM students WHERE id = #{id}
      """)
  Student searchById(Integer id);

  @Select(""" 
      SELECT * FROM students WHERE full_name = #{fullName}
      """)
  Student searchByName(String fullName);

  @Select(""" 
      SELECT * FROM students
      """)
  List<Student> searchAllStudents();

  @Insert("""
      INSERT INTO students (full_name, furigana, nick_name, email, city, age, gender)
      VALUES
      (#{fullName}, #{furigana}, #{nickName}, #{email}, #{city}, #{age}, #{gender})
      """)
  void registerStudent(Student student);

  @Update("""
      <script>
      UPDATE students
       <set>
         <if test="fullName != null">full_name = #{fullName}, </if>
         <if test="furigana != null">furigana = #{furigana}, </if>
         <if test="nickName != null">nick_name = #{nickName},</if>
         <if test="email !=null">email = #{email},</if>
         <if test="city != null">city = #{city},</if>
         <if test="age != null">age = #{age},</if>
         <if test="gender != null">gender = #{gender}, </if>
       </set>
        WHERE id = #{id}
      </script>
      """)
  void updateStudent(Student student);

  @Delete("""
      DELETE FROM students WHERE id = #{id}
      """)
  void deleteStudent(Integer id);

  @Delete("""
      DELETE FROM students WHERE full_name = #{fullName}
      """)
  void deleteStudentByName(String fullName);

}
