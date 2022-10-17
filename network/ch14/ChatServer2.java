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
			System.exit(1); // ���������� ����
		}
		
		System.out.println("ChatServer2.0���� ���۵�...");
		System.out.println("Client�� ������ ��ٸ��� �ֽ��ϴ�.");
		
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread2 ct = new ClientThread2(sock);
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
			ClientThread2 ct = vc.get(i);
			// ClientThread�� ������ �ִ� ������ �޼��� ������ �޼ҵ� ȣ��
			ct.sendMessage(msg);
		}
	}
	
	// ������ ������ ClientThread�� ���Ϳ��� ����
	public void removeClient(ClientThread2 ct) {
		vc.remove(ct);
	}
	
	// ���ӵ� ��� id ����Ʈ ex)aaa;bbb;ccc;��ȣ��; �̷������� ����
	public String getIds() {
		String ids = "";
		for(int i = 0; i < vc.size(); ++i) {
			ClientThread2 ct = vc.get(i);
			ids += ct.id + ";";
		}
		return ids;
	}
	
	// ������ ClientThread2 �˻�
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
				System.out.println(sock + "������...");
			}
		}
		
		public void routine(String line) {
			// CHATALL:[aaa]������ �������Դϴ�. ���� �ұ��?
			System.out.println(line);
			int idx = line.indexOf(ChatProtocol2.DM);
			String cmd = line.substring(0, idx); // CHALALL
			String data = line.substring(idx + 1); // [aaa]������ �������Դϴ�.
			if(cmd.equals(ChatProtocol2.ID)) {
				id = data; // aaa
				// ���ο� �����ڰ� �߰� �Ǿ��� ������ ����Ʈ ������
				sendAllMessage(ChatProtocol2.CHATLIST + ChatProtocol2.DM + getIds());
				// welcome �޼��� ����
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + 
						"[" + id + "]���� �����Ͽ����ϴ�.");
			} else if(cmd.equals(ChatProtocol2.CHAT)) {
				// data:bbb;�����
				idx = data.indexOf(';');
				cmd = data.substring(0, idx); // bbb(�޴»��)
				data = data.substring(idx + 1); // �����
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { // ���� ������
					// ct�� bbb�� �ǹ��Ѵ�. bbb���� ����
					ct.sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + 
							"[" + id + "(S)]" + data); // CHAT:[aaa(S)]�����
					
					// �ڱ� �ڽſ��Ե� �޼��� ����
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + 
							"[" + id + "(S)]" + data); // CHAT:[aaa(S)]�����
				} else { // bbb�� ������ �ȵȰ��
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + 
							"[" + cmd + "]���� �����ڰ� �ƴմϴ�.");
				}
			} else if(cmd.equals(ChatProtocol2.MESSAGE)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx); // bbb(�޴»��)
				data = data.substring(idx + 1); // �����
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { // ���� ������
					// ct�� bbb�� �ǹ��Ѵ�. bbb���� ����
					ct.sendMessage(ChatProtocol2.MESSAGE + ChatProtocol2.DM + 
							"[" + id + "(S)]" + data); // CHAT:[aaa(S)]�����
				} else { // bbb�� ������ �ȵȰ��
					sendMessage(ChatProtocol2.MESSAGE + ChatProtocol2.DM + 
							"[" + cmd + "]���� �����ڰ� �ƴմϴ�.");
				}
			} else if(cmd.equals(ChatProtocol2.CHATALL)) {
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + 
						"[" + id + "]" + data);
			}
		}

		// Client���� �޼��� ���� �޼ҵ�
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new ChatServer2();
	}
}