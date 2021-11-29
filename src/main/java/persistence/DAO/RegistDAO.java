package persistence.DAO;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import persistence.DTO.AppliedregistDTO;
import persistence.DTO.CreatedsubjectDTO;
import persistence.DTO.ProfessorDTO;
import persistence.DTO.StudentDTO;
import persistence.mapper.AppliedregistMapper;
import persistence.mapper.CreatedsubjectMapper;
import persistence.mapper.ProfessorMapper;
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
    public String check_professor_dao(String id,String password){
        String count="0";
        SqlSession session = sqlSessionFactory.openSession();
        ProfessorMapper mapper=session.getMapper(ProfessorMapper.class);
        try {
            count=mapper.check_by_pro_id_and_password(id,password);
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
    //학번으로 학생 반환
    public StudentDTO get_one_stduent_by_id(int stunum){
        StudentDTO one =null;
        SqlSession session = sqlSessionFactory.openSession();
        StudentMapper mapper=session.getMapper(StudentMapper.class);
        try{
            one=mapper.getonestudent_with_id(stunum);
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
    //학번에 해당하는 학생 비밀번호 변경
    public void update_cur_student_password(String newpwd,int stunum){
        SqlSession session = sqlSessionFactory.openSession();
        StudentMapper mapper=session.getMapper(StudentMapper.class);
        try{
            mapper.update_with_new_password(newpwd, stunum);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
        return;
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
    //파라미터로받은 학생학년과 같은 개설과목학년인 과목 반환function
    public List<CreatedsubjectDTO> get_grade_created_subject(int grade){
        List<CreatedsubjectDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        CreatedsubjectMapper mapper=session.getMapper(CreatedsubjectMapper.class);
        try {
            list=mapper.get_grade_createdsubject(grade);
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
    //교수기준 자기과목 수강목록반환  dynamic function
    public List<AppliedregistDTO> get_apllylist_by_selcode_and_page(String selsubcode,long page){

        List<AppliedregistDTO> list=null;
        SqlSession session = sqlSessionFactory.openSession();
        AppliedregistMapper mapper=session.getMapper(AppliedregistMapper.class);
        try{
            list=mapper.get_applylist_by_subcode_page(selsubcode,page);
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
    //선택과목 수강인원 -1function
    public void update_created_subject_max_stu_minus(String selectsubject)
    {
        CreatedsubjectDTO one=null;
        SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.SERIALIZABLE);
        CreatedsubjectMapper mapper=session.getMapper(CreatedsubjectMapper.class);
        try{
            mapper.update_max_num_minus(selectsubject);
            session.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            session.rollback();
        }
        finally {
            session.close();
        }
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
    public void delete_current_select_subject(int stunum,String selectsubject){

        SqlSession session = sqlSessionFactory.openSession();
        AppliedregistMapper mapper = session.getMapper(AppliedregistMapper.class);
        try{

            mapper.deleteselect_apply_subject(stunum,selectsubject);
            session.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            session.rollback();
            session.close();
        }
        finally {
            session.close();
        }

    }
    //교수dto 1개 교수 id로 찾기
    public ProfessorDTO get_professor_by_id(String proid){
        ProfessorDTO onepro=null;
        SqlSession session=sqlSessionFactory.openSession();
        ProfessorMapper mapper = session.getMapper(ProfessorMapper.class);
        try{

            onepro=mapper.select_one_pro(proid);
            session.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            session.rollback();
            session.close();

        }
        finally {
            session.close();
        }
        return onepro;
    }
    //교수 자신담당 교과목 list 반환
    public List<CreatedsubjectDTO> get_same_with_pronum(String pronum){
        List<CreatedsubjectDTO> clist=null;
        SqlSession session=sqlSessionFactory.openSession();
        CreatedsubjectMapper mapper=session.getMapper(CreatedsubjectMapper.class);
        try{
            clist=mapper.select_by_pronum(pronum);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
            session.close();
        }
        finally {
            session.close();
        }
        return clist;
    }

}
