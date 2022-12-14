package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	int port = 8001;
	int cnt = 0;

	public EchoServer() {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("ServerSocket Start...");
			
			while(true) {
				Socket sock = server.accept(); // Client가 접속 할때까지 대기
				EchoThread et = new EchoThread(sock);
				et.start(); // run 메소드 호출
				cnt++;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// 외부클래스와 아주 밀접한 관계가 있는 클래스, 장점은 상속이 가능하다.
	class EchoThread extends Thread {
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		
		public EchoThread(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				// Client가 접속을 하면 가장 먼저 받는 메세지
				out.println("Hello Enter BTE to exit");
				System.out.println("Clinet count : " + cnt);
				while(true) {
					// Client가 메세지 보내면 실행
					String line = in.readLine();
					if(line == null)
						break;
					else {
						out.println("ECHO : " + line);
						if(line.equals("BYE"))
							break;
					}
				}
				in.close();
				out.close();
				sock.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new EchoServer();
	}
}
