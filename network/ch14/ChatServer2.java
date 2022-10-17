package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer2 {
	ServerSocket server;
	int port = 8003;
	Vector<ClientThread2> vc;
	
	public ChatServer2() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<>();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error in Server");
			System.exit(1); // 비정상적인 종료
		}
		
		System.out.println("ChatServer2.0버전 시작됨...");
		System.out.println("Client의 접속을 기다리고 있습니다.");
		
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread2 ct = new ClientThread2(sock);
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
			ClientThread2 ct = vc.get(i);
			// ClientThread가 가지고 있는 각각의 메세지 보내는 메소드 호출
			ct.sendMessage(msg);
		}
	}
	
	// 접속이 끊어진 ClientThread는 벡터에서 제거
	public void removeClient(ClientThread2 ct) {
		vc.remove(ct);
	}
	
	// 접속된 모든 id 리스트 ex)aaa;bbb;ccc;강호동; 이런식으로 전달
	public String getIds() {
		String ids = "";
		for(int i = 0; i < vc.size(); ++i) {
			ClientThread2 ct = vc.get(i);
			ids += ct.id + ";";
		}
		return ids;
	}
	
	// 지정한 ClientThread2 검색
	public ClientThread2 findClient(String id) {
		ClientThread2 ct = null;
		for(int i = 0; i < vc.size(); ++i) {
			ct = vc.get(i);
			if(id.equals(ct.id)) {
				break;
			}// --if
		}// --for
		return ct;
	}
	
	class ClientThread2 extends Thread {
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public ClientThread2(Socket sock) {
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
				while(true) {
					String line = in.readLine();
					if(line == null) {
						break;
					} else {
						routine(line);
					}
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
		
		public void routine(String line) {
			// CHATALL:[aaa]오늘은 월요일입니다. 고만 할까요?
			System.out.println(line);
			int idx = line.indexOf(ChatProtocol2.DM);
			String cmd = line.substring(0, idx); // CHALALL
			String data = line.substring(idx + 1); // [aaa]오늘은 월요일입니다.
			if(cmd.equals(ChatProtocol2.ID)) {
				id = data; // aaa
				// 새로운 접속자가 추가 되었기 때문에 리스트 재전송
				sendAllMessage(ChatProtocol2.CHATLIST + ChatProtocol2.DM + getIds());
				// welcome 메세지 전송
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + 
						"[" + id + "]님이 입장하였습니다.");
			} else if(cmd.equals(ChatProtocol2.CHAT)) {
				// data:bbb;밥먹자
				idx = data.indexOf(';');
				cmd = data.substring(0, idx); // bbb(받는사람)
				data = data.substring(idx + 1); // 밥먹자
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { // 현재 접속자
					// ct는 bbb를 의미한다. bbb에게 전달
					ct.sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + 
							"[" + id + "(S)]" + data); // CHAT:[aaa(S)]밥먹자
					
					// 자기 자신에게도 메세지 전달
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + 
							"[" + id + "(S)]" + data); // CHAT:[aaa(S)]밥먹자
				} else { // bbb가 접속이 안된경우
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + 
							"[" + cmd + "]님은 접속자가 아닙니다.");
				}
			} else if(cmd.equals(ChatProtocol2.MESSAGE)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx); // bbb(받는사람)
				data = data.substring(idx + 1); // 밥먹자
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { // 현재 접속자
					// ct는 bbb를 의미한다. bbb에게 전달
					ct.sendMessage(ChatProtocol2.MESSAGE + ChatProtocol2.DM + 
							"[" + id + "(S)]" + data); // CHAT:[aaa(S)]밥먹자
				} else { // bbb가 접속이 안된경우
					sendMessage(ChatProtocol2.MESSAGE + ChatProtocol2.DM + 
							"[" + cmd + "]님은 접속자가 아닙니다.");
				}
			} else if(cmd.equals(ChatProtocol2.CHATALL)) {
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + 
						"[" + id + "]" + data);
			}
		}

		// Client에게 메세지 전달 메소드
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new ChatServer2();
	}
}
