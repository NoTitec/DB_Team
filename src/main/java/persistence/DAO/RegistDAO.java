package persistence.DAO;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import persistence.DTO.AppliedregistDTO;
import persistence.DTO.CreatedsubjectDTO;
import persistence.DTO.StudentDTO;
import persistence.mapper.AppliedregistMapper;
import persistence.mapper.CreatedsubjectMapper;
import persistence.mapper.StudentMapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RegistDAO {
    private SqlSessionFactory sqlSessionFactory=null;

    public RegistDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    //check student function
    public String check_student_dao(String id,String password){
        String count="0";
        SqlSession session = sqlSessionFactory.openSession();
        StudentMapper mapper=session.getMapper(StudentMapper.class);
        try {
            count=mapper.check_by_stu_id_and_password(id,password);
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
        return count;
    }
    //현재 로그인한 학생 반환
    public StudentDTO get_student_by_id_password(String id,String password){
        StudentDTO one=null;
        SqlSession session= sqlSessionFactory.openSession();
        StudentMapper mapper=session.getMapper(StudentMapper.class);
        try{
            one=mapper.getonestudent_with_id_and_password(id,password);
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
        return one;
    }
    //개설과목 반환 function
    public List<CreatedsubjectDTO> get_all_created_subject(){
        List<CreatedsubjectDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        CreatedsubjectMapper mapper=session.getMapper(CreatedsubjectMapper.class);
        try {
            list=mapper.get_all_createdsubject();
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
        return list;
    }
    //학생의 수강목록반환 function
    public  List<AppliedregistDTO> get_same_with_stunum(int stunum){
        List<AppliedregistDTO> list =null;
        SqlSession session = sqlSessionFactory.openSession();
        AppliedregistMapper mapper=session.getMapper(AppliedregistMapper.class);
        try {
            list=mapper.get_stu_applylist(stunum);
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
        return list;
    }
    //선택 개설 과목 반환 function
    public CreatedsubjectDTO get_one_by_created_code(String selectsubject){
        CreatedsubjectDTO one=null;
        SqlSession session = sqlSessionFactory.openSession();
        CreatedsubjectMapper mapper=session.getMapper(CreatedsubjectMapper.class);
        try{
            one=mapper.select_by_subcode(selectsubject);
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
        return one;
    }
    //선택과목 수강인원 +1 function (최대수강인원은 여기서 설정한다)
    public String update_created_subject_max_stu(String selectsubject){
        CreatedsubjectDTO one=null;
        SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.SERIALIZABLE);
        CreatedsubjectMapper mapper=session.getMapper(CreatedsubjectMapper.class);
        try{
            one=mapper.select_by_subcode(selectsubject);
            int curstu=one.getStumax();
            LocalDate applystartdate=one.getApplystartdate();
            LocalDate applyenddate=one.getApplyenddate();
            if(curstu<3){//최대 수강인원이 3명이라고 가정
                mapper.update_max_num(selectsubject);
                session.commit();
                System.out.println("수강신청인원증가");
                session.close();
                return "성공";
            }
            else
            {
                System.out.println("수강신청인원초과");
                session.rollback();
                session.close();
                return "실패";
            }
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
        return "종료";
    }

    //수강신청 테이블 insert function
    public String insert_current_select_subject(AppliedregistDTO a){
        SqlSession session = sqlSessionFactory.openSession();
        AppliedregistMapper mapper = session.getMapper(AppliedregistMapper.class);
        try{

            mapper.insert_stu_select_subject(a);
            session.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            session.rollback();
            session.close();
            return "inser 오류";
        }
        finally {
            session.close();
        }
        return "신청정상처리";
    }
    //수강신청 테이블 선택과목 삭제 function
    public void delete_current_select_subject(String selectsubject){
        SqlSession session = sqlSessionFactory.openSession();
        AppliedregistMapper mapper = session.getMapper(AppliedregistMapper.class);
        try{

            mapper.deleteselect_apply_subject(selectsubject);
            session.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            session.rollback();
            session.close();
            return;
        }
        finally {
            session.close();
        }
    }
}
