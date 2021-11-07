package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.DTO.ProfessorDTO;

import java.util.List;

public interface ProfessorMapper {
    @Select("SELECT * FROM PROFESSOR")
    @Results(id="professorResultSet",value={
            @Result(property = "pronum",column = "pro_num"),
            @Result(property = "proid",column = "pro_id"),
            @Result(property = "propass",column = "pro_pass"),
            @Result(property = "proname",column = "pro_name"),
            @Result(property = "proage",column = "pro_age"),
            @Result(property = "fmanid",column = "man_id"),
            @Result(property = "progender",column = "pro_gender")
    })
    List<ProfessorDTO> getAll();

    @Select("SELECT COUNT(*) FROM PROFESSOR WHERE pro_id=#{id} AND pro_pass=#{password}")
    String check_by_pro_id_and_password(@Param("id") String id, @Param("password") String password);

    @Select("SELECT * FROM PROFESSOR WHERE pro_id like #{proid}")
    @ResultMap("professorResultSet")
    ProfessorDTO select_one_pro(String proid);
}
