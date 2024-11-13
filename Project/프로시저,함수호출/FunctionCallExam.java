package Project.ch20.oracle.sec10;

//함수 호출
import java.lang.reflect.Type;
import java.sql.*;

public class FunctionCallExam {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            //JBC Driver 등록
            Class.forName("oracle.jdbc.OracleDriver");

            /*연결하기*/
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe",
                    "java2",
                    "oracle"
            );
            //매개변수화된 호출문 작성과 CallableStatemnt 언기
            String sql = "{? = call user_login(?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);

            //값 지정 및 리턴 타입 지정
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, "winter");
            cstmt.setString(3, "12345");

            //함수 실행 및 리턴값 얻기
            cstmt.execute();
            int result = cstmt.getInt(1);

            //CallableStatement 닫기
            cstmt.close();

            String message = switch (result) {
                case 0 -> "로그인 성공";
                case 1 -> "비밀번호가 틀림";
                default -> "아이디가 존재하지 않음";
            };
            System.out.println(message);
    }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (conn != null){
                try {
                    //연결 끊기
                    conn.close();
                }catch (SQLException e) {}
            }
        }
    }
}
