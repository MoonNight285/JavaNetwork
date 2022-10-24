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
			System.exit(1);//���������� ����
		}
		System.out.println("****************************");
		System.out.println("Talk Server 1.0 ...");
		System.out.println("Ŭ���̾�Ʈ�� ������ ��ٸ��� �ֽ��ϴ�.");
		System.out.println("****************************");
		try {
			while(true) {
				Socket sock = server.accept();
				TalkThread ct = new TalkThread(sock);
				ct.start();//������ �����ٷ��� ��� -> run �޼ҵ� ȣ��
				vc.addElement(ct);//ClientThread ��ü�� ���Ϳ� ����
			}
		} catch (Exception e) {
			System.err.println("Error in sock");
			e.printStackTrace();
		}
	}
	
	//��� ���ӵ� Client���� �޼��� ����
	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) {
			//Vector�� ����� ClientThread�� ���������� ������
			TalkThread ct = vc.get(i);
			//ClientThread ������ �ִ� ������ �޼��� ������ �޼ҵ� ȣ��
			ct.sendMessage(msg);
		}
	}
	
	//������ ������ ClientThread�� ���Ϳ��� ����
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
				System.out.println(sock + " ���ӵ�...");
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
						System.out.println("Ÿ��: " + splitDatas[0]);
						System.out.println("����: " + splitDatas[1]);
						if(splitDatas[0].equals("LOGIN")) {
							String[] datas = splitDatas[1].split(";");
							String readId = datas[0];
							String readPwd = datas[1];
							boolean result = mgr.loginChk(readId, readPwd);
							out.println("LOGIN_RESULT:" + result);
							
							// ���̵� ����
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
				System.err.println(sock + "[" + id +"] ������...");
			}
		}

		//Client���� �޼��� ���� �޼ҵ�
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new TalkServer();
	}

}





