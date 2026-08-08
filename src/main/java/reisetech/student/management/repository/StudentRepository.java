package reisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import reisetech.student.management.data.Student;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(int id);

  @Insert("INSERT INTO students (full_name, furigana, nickname, email, city, age, gender,remark)" +
      "VALUES(#{fullName}, #{furigana}, #{nickname}, #{email}, #{city}, #{age}, #{gender}, #{remark})")

  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  @Update("UPDATE students SET full_name = #{fullName}, furigana = #{furigana}, "
      + "nickname = #{nickname}, email = #{email}, city = #{city}, age = #{age}, "
      + "gender = #{gender}, remark = #{remark}, is_deleted = #{deleted} WHERE id = #{id}")
  void updateStudent(Student student);
}
