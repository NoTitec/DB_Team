package service;

import persistence.DAO.RegistDAO;
import persistence.DTO.AppliedregistDTO;
import persistence.DTO.CreatedsubjectDTO;
import persistence.DTO.ProfessorDTO;
import persistence.DTO.StudentDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RegistService {
    private final RegistDAO registDAO;
    public RegistService(RegistDAO registDAO){this.registDAO=registDAO;}

    public String check_professor(String id,String password){//교수 로그인체크함수
        String count=registDAO.check_professor_dao(id,password);
        return count;
    }
    public String check_student(String id,String password){//학생 로그인체크함수
        String count=registDAO.check_student_dao(id,password);
        return count;
    }
    public int regist_check(String id,String password,String selsubcode){//수강신청 가능 확인 로직
            LocalDate nowdate=LocalDate.now();//현재시간
            String selectsubject=selsubcode;//현재 선택한 교과목 코드
            //선택한 교과목 수강 신청 가능 기간인지 확인
            CreatedsubjectDTO getsubject=registDAO.get_one_by_created_code(selectsubject);//현재선택과목
            LocalDate applystartdate=getsubject.getApplystartdate();//수강신청가능일
            LocalDate applyenddate=getsubject.getApplyenddate();//수강신청종료일

            if(nowdate.isAfter(applystartdate)&& nowdate.isBefore(applyenddate))
            {
                System.out.println("수강신청 가능 날짜");
            }
            else
            {
                System.out.println("현재 수강신청 불가 날짜입니다 수강신청 가능일"+applystartdate+"부터"+applyenddate);
                return 2;
            }
            //선택한 교과목 요일,시작시간~종료시간 이 현재 학생 시간표시작시간과 겹치는게 있는지 확인
            boolean flag=true;

            StudentDTO getstudent=registDAO.get_student_by_id_password(id, password);//현재학생

            int stunum=getstudent.getStunum();//현재학생 학번
            List<AppliedregistDTO> studentapplylist= registDAO.get_same_with_stunum(stunum);//현재학생 수강 리스트

            int day=getsubject.getSubday();//강의요일
            LocalTime starttime=getsubject.getClassstarttime();//선택과목강의시작시간
            LocalTime endtime=getsubject.getClassendtime();//선택과목강의종료시간
            for (AppliedregistDTO one:studentapplylist) {//학생이 수강중인 과목을 1개씩 가져와서 선택과목과 시간겹치는게있으면 flag flase바꿈
                String onesubcode=one.getFcreatedsubcode();
                CreatedsubjectDTO onesubject= registDAO.get_one_by_created_code(onesubcode);
                if(day==onesubject.getSubday()){//강의요일이 동일하면 시간겹치는지 비교교
                    System.out.println("강의요일같으므로 시간 겹치는지 비교");
                    //선택과목 시작시간이 과목시간사이에있거나 종료시간이 과목시간사이면 false
                    if(((starttime.isAfter(onesubject.getClassstarttime())&&starttime.isBefore(onesubject.getClassendtime()))||(endtime.isAfter(onesubject.getClassstarttime())&&endtime.isBefore(onesubject.getClassendtime())))) {
                        flag = false;
                        break;
                    }//똑같은 과목 중복신청하면 false
                    else if(starttime.equals(onesubject.getClassstarttime())||endtime.equals(onesubject.getClassendtime())){
                        flag=false;
                        break;
                    }
                }
            }
            if(flag==false){
                System.out.println("현재 시간표와 겹치는 시간이므로 수강신청이 불가합니다");
                return 3;
            }
            return 0;
        }
    public int regist_excute(String id,String pwd,String selsubcode){//수강신청시 synchronize필수
        StudentDTO getstudent=registDAO.get_student_by_id_password(id, pwd);//현재학생

        int stunum=getstudent.getStunum();//현재학생 학번
        //개설교과목 update 트랜젝션 수행 table 락걸음 update 실패시 인원초과출력
        String result=registDAO.update_created_subject_max_stu(selsubcode);
        if(result.equals("실패"))
        {
            System.out.println("수강최대인원초과되어 수강신청이 불가합니다");
            return 4;
        }
        //수강신청 table insert수행
        AppliedregistDTO na=new AppliedregistDTO();
        na.setFcreatedsubcode(selsubcode);
        na.setFstunum(stunum);
        String inresult=registDAO.insert_current_select_subject(na);
        //신청완료출력
        System.out.println(inresult);
        return 1;

    }

    public List<CreatedsubjectDTO>getmysubject(String id,String password){//현재학생 수강신청 정보 get 로직
        List<CreatedsubjectDTO> stusubject=new ArrayList<>();//새로운 빈 리스트 생성

            //자기객체 가져오기
            StudentDTO getstudent=registDAO.get_student_by_id_password(id, password);//현재학생
            //수강목록에서 자기학번과 같은행 추출
            int stunum=getstudent.getStunum();//현재학생 학번
            List<AppliedregistDTO> studentapplylist= registDAO.get_same_with_stunum(stunum);//현재학생 수강 리스트
            //각행의 과목코드로 과목정보 가져오기
            for (AppliedregistDTO one:studentapplylist) {//수강목록 1행은 학생학번,과목코드가짐 1행가져와 코드읽고 그 코드로 개설과목정보 가져옴
                String onesubcode=one.getFcreatedsubcode();

                CreatedsubjectDTO onesubject= registDAO.get_one_by_created_code(onesubcode);
                        stusubject.add(onesubject);
            }

        return stusubject;//수강하고있는과목이 없으면 빈 리스트임
    }
    public void deletemysubject(String id, String password,String selsubcode){//현재학생 수강 취소 로직

            StudentDTO getstudent=registDAO.get_student_by_id_password(id, password);//현재학생
            int stunum=getstudent.getStunum();//현재학생 학번

            //개설교과목테이블 인원수 -1
            registDAO.update_created_subject_max_stu_minus(selsubcode);
            //삭제
            registDAO.delete_current_select_subject(stunum,selsubcode);
            return;
        }


    public List<StudentDTO>pageselect(String proid,String propass){//교수의 담당과목 출력하고 과목선택시 학생 반환
        List<StudentDTO> onelist=new ArrayList<>();
        //교수로그인
        String check=check_professor(proid,propass);
        if(check.equals("0")){
            System.out.println("id또는 비밀번호가 틀렸습니다");
            return onelist;
        }
        else {
            //자기담당교과목출력
            System.out.println("로그인성공");
            ProfessorDTO getprofessor=registDAO.get_professor_by_id(proid);//교수자기객체
            String pronum=getprofessor.getPronum();//자기교번
            System.out.println("pronum");
            List<CreatedsubjectDTO> prosubjectlist= registDAO.get_same_with_pronum(pronum);//현재교수 강의 리스트
            if(prosubjectlist.size()==0){
                System.out.println("강의하는 과목이 없습니다");
                return onelist;
            }
            for (CreatedsubjectDTO one:prosubjectlist) {
                String onesubname=one.getCreatedsubname();
                System.out.println(onesubname);
                String onesubcode=one.getCreatedsubcode();
                System.out.println(onesubcode);
            }
            //교과목선택
            System.out.println("조회하고자하는 강의 선택");
            System.out.println("CS0016선택 가정");
            //cs0016 선택가정
            String selectlecture="CS0016";
            //교과목테이블에서 해당과목수강신청한학생수가져옴
            CreatedsubjectDTO one=registDAO.get_one_by_created_code(selectlecture);

            int stucount=one.getStumax();
            System.out.println("수강신청한 학생수 : "+stucount);
            //for문 총페이지출력
            System.out.println("총 페이지 한페이지당 2명의 학생정보 출력");
            for(int i=1;1<=stucount;i++){
                System.out.print(i+" ");
                stucount-=2;
            }
            System.out.println();
            //페이지 선택
            System.out.println("1 page 선택가정");
            long selectpage=1;
            long offset=0;
            if(selectpage==1)
            {
                offset=0;
            }
            else {
                offset=selectpage*2-2;
            }
            //선택과목 해당되는 등록정보 1page 크기만큼 동적sql로 가져옴
            List<AppliedregistDTO>selsubcodelist=registDAO.get_apllylist_by_selcode_and_page(selectlecture,offset);

            //selsubcodelist의 stunum으로 학생객체 1개씩 가져와서 onelist에 add
            for (AppliedregistDTO aone:selsubcodelist) {
                int stunum=aone.getFstunum();
                    StudentDTO getaonestudent=registDAO.get_one_stduent_by_id(stunum);
                    onelist.add(getaonestudent);
            }
            //학생리스트반환
            return onelist;
        }
    }
}
