package persistence.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfessorDTO {
    private String pronum;
    private String proid;
    private String propass;
    private String name;
    private int proage;
    private String fmanid="master";
    private String progender;
}
