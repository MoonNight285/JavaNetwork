package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer1 {
	
	ServerSocket server;
	int port = 8002;
	Vector<ClientThread1> vc;
	
	public ChatServer1() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<>();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error in Server");
			System.exit(1); // 비정상적인 종료
		}
		
		System.out.println("ChatServer1 시작됨...");
		System.out.println("Client의 접속을 기다리고 있습니다.");
		
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread1 ct = new ClientThread1(sock);
				ct.start(); // 스레드 스케줄러에 등록 -> run 메소드 호출
				vc.addElement(ct); // ClientThread 객체를 벡터에 저장
			}
		} catch(Exception e) {
			System.out.println("Error in sock");
			e.printStackTrace();
		}
	}
	
	// 모든 접속된 Client에게 메세지 전달
	public void sendAllMessage(String msg) {
		for(int i = 0; i < vc.size(); ++i) {
			// Vector에 저장된 ClientThread를 순차적으로 가져옴
			ClientThread1 ct = vc.get(i);
			// ClientThread가 가지고 있는 각각의 메세지 보내는 메소드 호출
			ct.sendMessage(msg);
		}
	}
	
	// 접속이 끊어진 ClientThread는 벡터에서 제거
	public void removeClient(ClientThread1 ct) {
		vc.remove(ct);
	}
	
	class ClientThread1 extends Thread {
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public ClientThread1(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
				System.out.println(sock + "접속됨...");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				// Client가 처음으로 받는 메세지
				out.println("사용하실 아이디를 입력하세요.");
				id = in.readLine();
				// 모든 사용자들에게 접속한 사람의 welcome 메세지 전달
				sendAllMessage("[" + id + "]님이 입장하였습니다.");
				String line = "";
				while(true) {
					line = in.readLine(); // 메세지 들어올때까지 대기상태
					if(line == null) {
						break;
					}
					sendAllMessage("[" + id + "]" + line);
				}
				in.close();
				out.close();
				sock.close();
				removeClient(this);
			} catch(Exception e) {
				removeClient(this);
				System.out.println(sock + "끊어짐...");
			}
		}
		
		// Client에게 메세지 전달 메소드
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new ChatServer1();
	}

}
