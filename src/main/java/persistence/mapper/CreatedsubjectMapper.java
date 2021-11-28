package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.DTO.CreatedsubjectDTO;

import java.util.List;

public interface CreatedsubjectMapper {

    @Select("SELECT * FROM CREATEDSUBJECT")
    @Results(id="createdsubjectSet",value = {
            @Result(property = "createdsubcode",column = "created_subcode"),
            @Result(property = "createdsubname",column = "created_sub_name"),
            @Result(property = "createdsubinfo",column = "created_sub_info"),
            @Result(property = "createdsubgrade",column = "created_sub_grade"),
            @Result(property = "stumax",column = "stu_max"),
            @Result(property = "room",column = "room"),
            @Result(property = "classstarttime",column = "class_start_time"),
            @Result(property = "classendtime",column = "class_end_time"),
            @Result(property = "classplan",column = "class_plan"),
            @Result(property = "planstartdate",column = "plan_start_date"),
            @Result(property = "planenddate",column = "plan_end_date"),
            @Result(property = "applystartdate",column = "apply_start_date"),
            @Result(property = "applyenddate",column = "apply_end_date"),
            @Result(property = "fmanid",column = "man_id"),
            @Result(property = "fpronum",column = "pro_num"),
            @Result(property = "subday",column = "created_sub_day")
    })
    List<CreatedsubjectDTO> get_all_createdsubject();


    @Select("SELECT * FROM CREATEDSUBJECT WHERE CREATED_SUB_GRADE = #{grade}")
    @ResultMap("createdsubjectSet")
    List<CreatedsubjectDTO> get_grade_createdsubject(int grade);

    @Select("SELECT * FROM CREATEDSUBJECT WHERE CREATED_SUBCODE LIKE #{code}")
    @ResultMap("createdsubjectSet")
    CreatedsubjectDTO select_by_subcode(@Param("code") String selectsubject);//1개 교과목 code로 찾아서 반환

    @Update("update createdsubject set  stu_max=  stu_max+1  where created_subcode LIKE #{subname}")
    void update_max_num(@Param("subname")String selectsubjectcode);//1개 과목 maxstu 1명 추가

    @Update("update createdsubject set  stu_max=  stu_max-1  where created_subcode LIKE #{subname}")
    void update_max_num_minus(@Param("subname")String selectsubject);

    @Select("SELECT * FROM CREATEDSUBJECT WHERE PRO_NUM LIKE #{fpronum}")
    @ResultMap("createdsubjectSet")
    List<CreatedsubjectDTO>select_by_pronum(String pronum);
}
