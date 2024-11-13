package ch19.sec07;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    //필드 선언
    Socket socket;         //서버와의 소켓 연결
    DataInputStream dis;   //서버로부터의 입력 스트림
    DataOutputStream dos;  // 서버로의 출력 스트림
    String chatName;       //사용자의 채팅 이름

    //메소드: 서버 연결
    public void connect() throws IOException {
        //서버의 IP주소와 포트 번호로 소켓 생성
        socket = new Socket("localhost", 50001);
        // 입력 스트림 초기화
        dis = new DataInputStream(socket.getInputStream());
        // 출력 스트림 초기화
        dos = new DataOutputStream(socket.getOutputStream());
        System.out.println("[클라이언트] 서버에 연결됨");
    }

    //메소드 : JSON 받기 - 서버로부터 메세지를 수신하는 메서드
    public void receive() {
        // 수신을 위한 별도의 스레드 생성
        Thread thread = new Thread(() -> {
            try {
                while (true){
                    // 서버로부터 JSON 형식의 문자열 수신
                    String json = dis.readUTF();
                    // JSON 객체로 변환
                    JSONObject root = new JSONObject(json);
                    // JSON 객체에서 각 필드 추출
                    String clientIp = root.getString("clientIp");
                    String chatName = root.getString("chatName");
                    String message = root.getString("message");
                    System.out.println("<"+ chatName + "@" + clientIp + ">" + message);
                }
            }catch (Exception e1) {
                System.out.println("[클라이언트] 서버 연결 끊김");
                System.exit(0);   //프로그램 종료
            }
        });
        thread.start();   //수신 스레드 시작
    }

    //메소드 : JSON 보내기 - 서버로 메세지를 전송하는 메서드
    public void send(String json) throws IOException {
        //JSON 형식의 문자열을 서버로 전송
        dos.writeUTF(json);
        dos.flush();      //출력 스트림 버퍼 비우기
    }

    //메소드 : 서버 연결 종료
    public void unconnect() throws IOException {
        //소켓 닫기
        socket.close();
    }

    //메소드 : 메인 - 프로그램의 시작점
    public static void main(String[] args) {
        try {
            //ChatClient 인스턴스 생성
            ChatClient chatClient = new ChatClient();
            //서버에 연결
            chatClient.connect();

            //사용자로부터 대화명 입력 받기
            Scanner scanner = new Scanner(System.in);
            System.out.println("대화명 입력: ");
            chatClient.chatName = scanner.nextLine();

            //서버에 입장 메세지 전송
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", "incoming");
            jsonObject.put("data", chatClient.chatName);
            String json = jsonObject.toString();
            chatClient.send(json);

            //서버로부터 메세지 수신 시작
            chatClient.receive();

            //사용자 안내 메세지 출력
            System.out.println("-------------------------------------------------------------------");
            System.out.println("보낼 메세지를 입력하고 Enter");
            System.out.println("채팅을 종료하려면 q를 입력하고 Enter");
            System.out.println("-------------------------------------------------------------------");

            //사용자로부터 메세지 입력 및 전송 루프
            while (true) {
                String message = scanner.nextLine();
                if (message.toLowerCase().equals("q")) {
                    break; //'q'입력 시 루프 종료
                } else {
                    // 입력한 메세지를 JSON 객체로 생성하여 서버로 전송
                    jsonObject = new JSONObject();
                    jsonObject.put("command", "message");
                    jsonObject.put("data", message);
                    json = jsonObject.toString();
                    chatClient.send(json);
                }
            }

            //자원 정리 : 스캐너 닫기 및 서버와의 연결 종료
            scanner.close();
            chatClient.unconnect();
        } catch (IOException e) {
            System.out.println("[클라이언트] 서버 연결 안됨");
        }
    }
}
