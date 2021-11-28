package persistence.mapper;

import lombok.Setter;
import org.apache.ibatis.annotations.*;
import persistence.DTO.AppliedregistDTO;

import java.util.List;


public interface AppliedregistMapper {

    @Select("SELECT * FROM APPLIEDREGIST")
    @Results(id="AppliedResultSet",value = {
            @Result(property = "applyautonum",column = "apply_autonum"),
            @Result(property = "fcreatedsubcode",column = "created_subcode"),
            @Result(property = "fstunum",column = "stu_num")
    })
    List<AppliedregistDTO> get_all_applylist();

    @Select("SELECT * FROM APPLIEDREGIST WHERE STU_NUM=#{stunumber}")
    @ResultMap("AppliedResultSet")
    List<AppliedregistDTO> get_stu_applylist(int stunumber);

    @SelectProvider(type=persistence.mapper.AppliedregistSQL.class,method="select_limit_applylist")
    //@Select("SELECT * FROM APPLIEDREGIST WHERE CREATED_SUBCODE LIKE #{selsubcode} ")
    @ResultMap("AppliedResultSet")
    List<AppliedregistDTO > get_applylist_by_subcode_page(@Param("selsubcode") String selsubcode,long page);

    @Insert("INSERT INTO APPLIEDREGIST (created_subcode, stu_num)\n"+
            "VALUES (#{fcreatedsubcode}, #{fstunum})")
    @Options(useGeneratedKeys = true, keyProperty = "applyautonum")
    void insert_stu_select_subject(AppliedregistDTO a);

    @Delete("DELETE FROM APPLIEDREGIST WHERE created_subcode LIKE #{subcode} AND stu_num LIKE #{stuid}")
    void deleteselect_apply_subject(@Param("stuid") int stuid,@Param("subcode") String subcode);
}
