package ch19.sec07;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    
    //필드선언
    
    ServerSocket serverSocket;  //클라이언트 연결을 수락하는 서버 소켓
    ExecutorService threadPool = Executors.newFixedThreadPool(100);   //최대 100개의 스레드를 관리하는 스레드 풀
    Map<String, SocketClient> chatRoom =
            Collections.synchronizedMap(new HashMap<>());    //채팅 참여자 목록

    //메소드 : 서버 시작
    public void start() throws IOException {
        // 서버 소켓을 포트 50001에서 생성
        serverSocket = new ServerSocket(50001);
        System.out.println("[서버] 시작됨");

        //클라이언트 연결을 수락하는 스레드 생성
        Thread thread = new Thread(()-> {
            try {
                while (true){
                    // 클라이언트의 연결 요청을 수락
                    Socket socket = serverSocket.accept();
                    // 새로운 클라이언트에 대한 SocketClient 객체 생성
                    SocketClient sc = new SocketClient(this, socket);
                }
            }catch (IOException e){
                System.out.println("[서버] 종료");
            }
        });
        thread.start();    //스레드 시작
    }

    //메소드 : 클라이언트 연결 시 SocketClient 생성 및 추가
    public void addSocketClient(SocketClient SocketClient) {
        // 클라이언트의 식별 키 생성 (대화명 @IP주소)
        String key = SocketClient.chatName + "@" + SocketClient.clientIp;
        // 클라이언트를 목록에 추가
        chatRoom.put(key, SocketClient);
        System.out.println("입장 : "+ key);
        System.out.println("현재 채팅자 수 : " + chatRoom.size()+ "\n");
    }

    //메소드 : 클라이언트 연결 종료 시 SocketClient 제거
    public void removeSocketClient(SocketClient serverSocket) {
        //클라이언트의 식별 키 생성
        String key = serverSocket.chatName + "@" + serverSocket.clientIp;
        // 목록에서 클라이언트 제거
        chatRoom.remove(key);
        System.out.println("나감 : "+ key);
        System.out.println("현재 채팅자 수 : " + chatRoom.size()+ "\n");
    }

    //메소드 : 모든 클라이언트에게 메세지 보냄
    public void sendToAll (SocketClient sender, String message) {
        //JSON 객체 생성
        JSONObject root = new JSONObject();
        root.put("clientIp", sender.clientIp);
        root.put("chatName", sender.chatName);
        root.put("message", message);
        String json = root.toString();

        // 모든 클라이언트에게 메세지 전송
        Collection<SocketClient> socketClients = chatRoom.values();
        for (SocketClient sc : socketClients) {
            if (sc == sender) continue;  //발신자에게는 전송하지 않음
            sc.send(json);
        }
    }

    //메소드 : 서버종료
    public void stop() {
        try {
            serverSocket.close();               //서버 소켓 닫기
            threadPool.shutdownNow();           //스레드 풀 종료
            chatRoom.values().stream().forEach(sc -> sc.close());        //모든 클라이언트의 연결 종료
            System.out.println("[서버] 종료됨");
        }catch (IOException e1){}
    }

    //-----------------------메소드 메인 ----------------------------------------- 프로그램의 시작점

    public static void main(String[] args) {
        try {
            // ChatServer 인스턴스 생성
            ChatServer chatServer = new ChatServer();
            // 서버 시작
            chatServer.start();

            System.out.println("-------------------------------------------------------------------");
            System.out.println("서버를 종료하려면 q를 입력하고 Enter");
            System.out.println("-------------------------------------------------------------------");

            //사용자 입력을 대기하여 'q' 입력시 서버 종료
            Scanner scanner = new Scanner(System.in);
            while (true){
                String key = scanner.nextLine();
                if (key.equals("q")) break;
            }
            scanner.close();
            // 서버 종료
            chatServer.stop();
        }catch (IOException e) {
            System.out.println("[서버]" + e.getMessage());
        }
    }
}
