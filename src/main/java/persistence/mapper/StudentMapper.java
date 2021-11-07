package persistence.mapper;
import jdk.jfr.BooleanFlag;
import org.apache.ibatis.annotations.*;
import persistence.DTO.StudentDTO;
import java.util.List;
public interface StudentMapper {

    @Select("SELECT * FROM STUDENT")
    @Results(id="StudentResultSet",value={
            @Result(property = "stunum",column = "stu_num"),
            @Result(property = "stuid",column = "stu_id"),
            @Result(property = "stupass",column = "stu_pass"),
            @Result(property = "stuname",column = "stu_name"),
            @Result(property = "stuage",column = "stu_age"),
            @Result(property = "fmanid",column = "man_id"),
            @Result(property = "stugender",column = "stu_gender")
    })
    List<StudentDTO> getallstudent();

    @Select("SELECT COUNT(*) FROM STUDENT WHERE stu_id=#{id} AND stu_pass=#{password}")
    String check_by_stu_id_and_password(@Param("id") String id,@Param("password") String password);

    @Select("SELECT * FROM STUDENT WHERE stu_id=#{id} AND stu_pass=#{password}")
    @ResultMap("StudentResultSet")
    StudentDTO getonestudent_with_id_and_password(@Param("id") String id,@Param("password") String password);

    @Select("SELECT * FROM STUDENT WHERE stu_num=#{num}")
    @ResultMap("StudentResultSet")
    StudentDTO getonestudent_with_id(@Param("num") int num);
}
