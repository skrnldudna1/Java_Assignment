package src.ch18.sec04.exam02;

/*문자 읽기*/
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ReadExam {
    public static void main(String[] args) {

//        파일을 읽기 위한 리더 객체 선언
        try {
            Reader reader = null;

        /*1. 파일을 한 문자씩 읽기*/
            reader = new FileReader("C:/Temp/test.txt");  /*읽을 파일 설정*/
            while (true) {
                int data = reader.read();
                if (data == -1) break;       /*파일의 끝에 도달하면 반복 종료*/
                System.out.print((char)data);
            }
            reader.close();
            System.out.println();

            /*2. 파일을 문자 배열로 읽기*/
            reader = new FileReader("C:/Temp/test.txt");
            char[] data = new char[100];      /*읽을 문자를 저장할 배열 생성*/
            while (true) {
                int num = reader.read(data);
                if (num == -1) break;
                for (int i =0; i<num; i++) {   /*읽은 문자 수만큼 반복하여 출력*/
                    System.out.print(data[i]);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {          /*파일을 찾을 수 없을 때 예외 처리*/
            e.printStackTrace();
        } catch (IOException e) {                    /*입출력 오류 발생 시 예외 처리*/
            e.printStackTrace();
        }
    }
}
