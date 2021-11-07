package persistence.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class AppliedregistSQL {
    public String select_limit_applylist(@Param("selsubcode") String selsubcode, long page){

        SQL sql=new SQL(){
            {
                SELECT("*");
                FROM("APPLIEDREGIST");
                WHERE("CREATED_SUBCODE LIKE #{selsubcode}");
                LIMIT(2);
                OFFSET(page);

            }
        };
        return sql.toString();
    }
}
