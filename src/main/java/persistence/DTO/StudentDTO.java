package persistence.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class StudentDTO {
    private int stunum;
    private String stuid;
    private String stupass;
    private String stuname;
    private int stuage;
    private String fmanid="master";
    private String stugender;
    private int stugrade;
}
