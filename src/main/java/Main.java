import persistence.DAO.RegistDAO;
import persistence.DTO.CreatedsubjectDTO;
import persistence.MybatisConnectionFactory;
import service.RegistService;
import view.RegistView;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RegistDAO registDAO = new RegistDAO(MybatisConnectionFactory.getSqlSessionFactory());
        RegistService registService = new RegistService(registDAO);
        RegistView registView = new RegistView();

        Scanner sc = new Scanner(System.in);



        while (true) {
            System.out.println("원하는 대분류 기능 선택 종료는 -1 입력");
            System.out.println("1.학생 crd 2.교과목 crd 3.개설 교과목 crd,수강신청기간변경  4.수강신청관련");
            int startmenu = sc.nextInt();
            if(startmenu==-1){
                break;
            }

            switch (startmenu) {
                case 1:
                    System.out.println("원하는 소분류 기능 선택");
                    int firstmenu = sc.nextInt();
                    switch (firstmenu) {

                    }
                    break;
                case 2:
                    System.out.println("원하는 소분류 기능 선택");
                    int secondmenu = sc.nextInt();
                    switch (secondmenu) {

                    }
                    break;
                case 3:
                    System.out.println("원하는 소분류 기능 선택");
                    int thirdmenu = sc.nextInt();
                    switch (thirdmenu) {

                    }
                    break;
                case 4:
                    System.out.println("원하는 소분류 기능 선택");
                    System.out.println("1.수강신청2.수강조회3.수강삭제");
                    System.out.println("교과목 수강신청학생목록조회");
                    int fourthmenu = sc.nextInt();
                    String id = "kumid";
                    String password = "kumpass";
                    switch (fourthmenu) {
                        case 1:
                            //학생아이디,비밀번호 입력가정
                            System.out.println("아이디로" + id + "입력됨");
                            System.out.println("비밀번호로" + password + "입력됨");
                            registService.regist(id, password);
                            System.out.println("신청서비스종료");
                            break;
                        case 2:
                            //학생아이디,비밀번호 입력가정
                            System.out.println("아이디로" + id + "입력됨");
                            System.out.println("비밀번호로" + password + "입력됨");
                            List<CreatedsubjectDTO> r = registService.getmysubject(id, password);
                            registView.print_stu_subject(r);
                            System.out.println("조회서비스 종료");
                    }
                    break;
            }
        }
    }
}
