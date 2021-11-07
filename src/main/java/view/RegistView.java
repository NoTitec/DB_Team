package view;

import persistence.DTO.CreatedsubjectDTO;
import persistence.DTO.StudentDTO;

import java.util.List;

public class RegistView {

    public void print_stu_subject(List<CreatedsubjectDTO> r){
        if(r.size()==0){
            System.out.println("수강신청한 과목이 없습니다");
        }
        else {
            System.out.println("현재 학생의 수강신청 정보 목록");
            r.stream().forEach(v -> System.out.println("toString()=" + v.toString()));
        }
    }

    public void print_stu_list(List<StudentDTO> r){
        if(r.size()==0){
            System.out.println("학생이 없습니다");
        }
        else {
            System.out.println("현재 과목의 수강신청 학생정보 목록");
            for (StudentDTO onestu:r) {
                System.out.print("학생이름 "+onestu.getStuname());
                System.out.print(" 학번 "+onestu.getStunum());
                System.out.print(" 나이 "+onestu.getStuage());
                System.out.print(" 성별 "+onestu.getStugender());
                System.out.println();
            }
            //r.stream().forEach(v -> System.out.println("학생정보 =" + v.toString()));
        }
    }
}
