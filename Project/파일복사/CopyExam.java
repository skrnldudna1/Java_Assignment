package src.ch18.sec03.exam03;

/*파일 복사 예제*/

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyExam {
    public static void main(String[] args) throws Exception  {

        /*원본 파일과 복사할 대상 파일의 경로와 이름을 설정*/
        String originalFileName = "C:/Temp/주먹토끼.gif";
        String targetFileName = "C:/Temp/주먹토끼2.gif";

        /*원본 파일과 복사할 대상 파일의 경로와 이름을 설정*/
        InputStream is = new FileInputStream(originalFileName);

        /*복사 대상 파일을 쓰기 위한 출력 스트림 생성*/
        OutputStream os = new FileOutputStream(targetFileName);

        /*1024 바이트 크기의 배열 생성*/
        byte[] data = new byte[1024];

        /*파일 끝까지 반복하여 읽고 쓰기*/
        while (true) {
            int num = is.read(data);
            if (num == -1) break;           /*파일 끝에 도달하면 반복 종료*/
            os.write(data, 0, num);     /*읽어들인 바이트 수만큼 복사 대상 파일에 쓰기*/
        }

        os.flush();
        os.close();

        System.out.println("복사가 잘 되었습니다.");      /*복사 완료 메시지 출력*/
    }
}
