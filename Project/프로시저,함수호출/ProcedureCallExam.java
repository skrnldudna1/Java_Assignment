package Project.ch20.oracle.sec10;

//user_create 프로시저 호출 하는 전체 코드
import java.sql.*;

public class ProcedureCallExam {
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
            String sql = "{call user_create(?, ?, ?, ?, ?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);

            // ? 값 지정 및 리턴 타입 지정
            cstmt.setString(1, "summer");
            cstmt.setString(2, "한여름");
            cstmt.setString(3, "12345");
            cstmt.setInt(4, 26);
            cstmt.setString(5, "summer@mycompany.com");
            cstmt.registerOutParameter(6, Types.INTEGER);

            //프로시저 실행 및 리턴값 얻기
            cstmt.execute();
            int row = cstmt.getInt(6);
            System.out.println("저장된 행 수 : " + row);

            //CallableStatement 닫기
            cstmt.close();
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
