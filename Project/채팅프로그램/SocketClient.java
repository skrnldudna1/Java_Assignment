package ch19.sec07;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {

    //필드선언
    ChatServer chatServer;        // 서버와의 상호작용을 위한 ChatServer 객체
    Socket socket;                // 클라이언트와 서버 간의 통신을 위한 소켓
    DataInputStream dis;          // 클라이언트로부터 데이터를 읽기 위한 입력 스트림
    DataOutputStream dos;         // 클라이언트로 데이터를 보내기 위한 출력 스트림
    String clientIp;              // 클라이언트의 IP 주소
    String chatName;              // 클라이언트의 대화명

    //생성자 : 새로운 클라이언트 연결 시 호출

    public SocketClient(ChatServer chatServer, Socket socket) {
        try {
            this.chatServer = chatServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            InetSocketAddress isa =
                    (InetSocketAddress) socket.getRemoteSocketAddress();
            this.clientIp = isa.getHostName();
            receive();      // 클라이언트로부터 메시지를 수신하는 메서드 호출
        } catch (IOException e) {

        }
    }

     // 클라이언트로부터 메시지를 수신하는 메서드
    public void receive() {
        chatServer.threadPool.execute(()-> {
            try {
                while (true) {
                     // 클라이언트로부터 JSON 형식의 메시지 수신
                    String receiveJson = dis.readUTF();
                    JSONObject jsonObject = new JSONObject(receiveJson);
                    String command = jsonObject.getString("command");

                     // 명령어에 따른 처리
                    switch(command) {
                        case "incoming" :
                            this.chatName = jsonObject.getString("data");
                            chatServer.sendToAll(this, "들어오셨습니다.");
                            chatServer.addSocketClient(this);
                            break;
                        case "message" :
                            String message = jsonObject.getString("data");
                            chatServer.sendToAll(this, message);
                            break;
                    }
                }
            }catch (IOException e) {
                chatServer.sendToAll(this, "나가셨습니다.");
                chatServer.removeSocketClient(this);
            }
        });
    }

    //메소드 : JSON 보내기
    public void send(String json) {
        try {
            dos.writeUTF(json);
            dos.flush();
        }catch (IOException e) {

        }
    }
    //메소드 : 연결 종료
    public void close() {
        try {
            socket.close();
        }catch (IOException e){}
    }
}

