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
			System.exit(1); // ���������� ����
		}
		
		System.out.println("ChatServer1 ���۵�...");
		System.out.println("Client�� ������ ��ٸ��� �ֽ��ϴ�.");
		
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread1 ct = new ClientThread1(sock);
				ct.start(); // ������ �����ٷ��� ��� -> run �޼ҵ� ȣ��
				vc.addElement(ct); // ClientThread ��ü�� ���Ϳ� ����
			}
		} catch(Exception e) {
			System.out.println("Error in sock");
			e.printStackTrace();
		}
	}
	
	// ��� ���ӵ� Client���� �޼��� ����
	public void sendAllMessage(String msg) {
		for(int i = 0; i < vc.size(); ++i) {
			// Vector�� ����� ClientThread�� ���������� ������
			ClientThread1 ct = vc.get(i);
			// ClientThread�� ������ �ִ� ������ �޼��� ������ �޼ҵ� ȣ��
			ct.sendMessage(msg);
		}
	}
	
	// ������ ������ ClientThread�� ���Ϳ��� ����
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
				System.out.println(sock + "���ӵ�...");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				// Client�� ó������ �޴� �޼���
				out.println("����Ͻ� ���̵� �Է��ϼ���.");
				id = in.readLine();
				// ��� ����ڵ鿡�� ������ ����� welcome �޼��� ����
				sendAllMessage("[" + id + "]���� �����Ͽ����ϴ�.");
				String line = "";
				while(true) {
					line = in.readLine(); // �޼��� ���ö����� ������
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
				System.out.println(sock + "������...");
			}
		}
		
		// Client���� �޼��� ���� �޼ҵ�
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new ChatServer1();
	}

}