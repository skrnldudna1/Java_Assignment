package src.Project.oracle.sec09.exam01;

import lombok.Data;

@Data  //Constructor, Gettet, Setter, hashCode(), equals(), toString() 롬복을 이용한 자동생성
public class User {
    private String userId;
    private String userName;
    private String userPassword;
    private int userAge;
    private String userEmail;
}
