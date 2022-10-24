package awtNetProject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TalkServer {
	
	ServerSocket server;
	int port = 8006;
	Vector<TalkThread> vc;
	TalkMgr mgr;
	
	public TalkServer() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<TalkThread>();
			mgr = new TalkMgr();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			System.exit(1);//비정상적인 종료
		}
		System.out.println("****************************");
		System.out.println("Talk Server 1.0 ...");
		System.out.println("클라이언트의 접속을 기다리고 있습니다.");
		System.out.println("****************************");
		try {
			while(true) {
				Socket sock = server.accept();
				TalkThread ct = new TalkThread(sock);
				ct.start();//쓰레드 스케줄러에 등록 -> run 메소드 호출
				vc.addElement(ct);//ClientThread 객체를 벡터에 저장
			}
		} catch (Exception e) {
			System.err.println("Error in sock");
			e.printStackTrace();
		}
	}
	
	//모든 접속된 Client에게 메세지 전달
	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) {
			//Vector에 저장된 ClientThread를 순차적으로 자져옴
			TalkThread ct = vc.get(i);
			//ClientThread 가지고 있는 각각의 메세지 보내는 메소드 호출
			ct.sendMessage(msg);
		}
	}
	
	//접속이 끊어진 ClientThread는 벡터에서 제거
	public void removeClient(TalkThread ct) {
		vc.remove(ct);
	}
	
	class TalkThread extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public TalkThread(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
				System.out.println(sock + " 접속됨...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					String line = in.readLine();
					if(line==null)
						break;
					else {
						String[] splitDatas = line.split(":");
						System.out.println("타입: " + splitDatas[0]);
						System.out.println("내용: " + splitDatas[1]);
						if(splitDatas[0].equals("LOGIN")) {
							String[] datas = splitDatas[1].split(";");
							String readId = datas[0];
							String readPwd = datas[1];
							boolean result = mgr.loginChk(readId, readPwd);
							out.println("LOGIN_RESULT:" + result);
							
							// 아이디 저장
							if(result == true) {
								id = readId;
							}
						} else if(splitDatas[0].equals("CHATALL")) {
							String msg = splitDatas[1];
							sendAllMessage(msg);
						}
					}
				}
				in.close();
				out.close();
				sock.close();
				removeClient(this);
			} catch (Exception e) {
				removeClient(this);
				System.err.println(sock + "[" + id +"] 끊어짐...");
			}
		}

		//Client에게 메세지 전달 메소드
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new TalkServer();
	}

}





