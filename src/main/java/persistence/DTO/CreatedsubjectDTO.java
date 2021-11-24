package persistence.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
@ToString
public class CreatedsubjectDTO {

    private String createdsubcode;
    private String createdsubname;
    private String createdsubinfo;
    private int createdsubgrade;
    private int stumax;
    private String room;
    private LocalTime classstarttime;
    private LocalTime classendtime;
    private String classplan;
    private LocalDate planstartdate;
    private LocalDate planenddate;
    private LocalDate applystartdate;
    private LocalDate applyenddate;
    private String fmanid="master";
    private String fpronum;
    private int subday;
}
