package Project.oracle.sec09.exam02;

/*게시물 정보 읽기*/
import lombok.Data;
import java.sql.Blob;
import java.sql.Date;

@Data
public class Board {
    //Constructor, Getter, Setter, hashCode(), equals(), toString() 자동 생성
    private int bno;
    private String btitle;
    private String bcontent;
    private String bwriter;
    private Date bdate;
    private String bfilename;
    private Blob bfiledata;
}
