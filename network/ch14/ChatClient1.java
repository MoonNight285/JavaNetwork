package ch14;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ch14.ChatServer1.ClientThread1;

public class ChatClient1 extends MFrame implements ActionListener, Runnable{
	
	Button btn1, btn2;
	TextField tf1, tf2;
	TextArea ta;
	Panel p1, p2;
	BufferedReader in;
	PrintWriter out;
	int port = 8002;
	String id;
	String filterList[] = {"?ٺ?","??????","????","?ڹ?","java"};
	
	public ChatClient1() {
		super(350,400);
		setTitle("MyChat 1.0");
		p1 = new Panel();
		p1.setBackground(new Color(100,200,100));
		p1.add(new Label("HOST ",Label.CENTER));
		p1.add(tf1 = new TextField("127.0.0.1",25));
		//p1.add(tf1 = new TextField("10.100.204.62",25));
		p1.add(btn1 = new Button("Connect"));
		
		p2 = new Panel();
		p2.setBackground(new Color(100,200,100));
		p2.add(new Label("CHAT ",Label.CENTER));
		p2.add(tf2 = new TextField("",25));
		p2.add(btn2 = new Button("SEND"));	
		
		tf1.addActionListener(this);
		tf2.addActionListener(this);
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		
		add(p1,BorderLayout.NORTH);
		add(ta=new TextArea());
		add(p2,BorderLayout.SOUTH);
		validate();//????
	}
	
	public boolean strFilter(String target) {
		boolean flag = false;
		for(int i = 0; i < filterList.length; ++i) {
			//if(filterList[i].equals(target)) {
				//flag = true;
				//break;
			//}
			
			if(target.contains(filterList[i])) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == tf1 || obj == btn1) {
			connect(tf1.getText().trim());
			tf1.setEnabled(false);
			btn1.setEnabled(false);
			tf2.requestFocus();
		} else if(obj == tf2 || obj == btn2) {
			String str = tf2.getText().trim();
			if(str.length() == 0) {
				return; // ?޼ҵ? ????
			}
			if(strFilter(str)) { // ???͸? ????
				ta.append("?????????ϴ? ???ڿ??? ?????? ?ܾ ???ԵǾ??ֽ??ϴ?.\n");
				tf2.setText("");
				tf2.requestFocus();
				return;
			}
			if(id == null) { // ù??°?? ????
				id = str;
				setTitle(getTitle() + "[" + id + "]");
				ta.setText("ä???? ?????մϴ?.\n");
			}
			out.println(str); // ?????? ?Է??? ???ڿ? ????
			tf2.setText("");
			tf2.requestFocus();
		}
	}//--actionPerformed
	
	@Override
	//?????? ???? ?޼????? ???????? ?????ϴ? ????
	public void run() {
		try {
			while(true) {
				// ???????? ?޼??? ???޵Ǹ? ta?? append
				ta.append(in.readLine() + "\n");
				tf2.requestFocus(); // ä?? ?Է?â
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//--run
	
	public void connect(String host){
		try {
			Socket sock = new Socket(host, port);
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			// ???? ?????Ŀ? ?????? ????
			Thread t = new Thread(this);
			t.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//--connect
	
	public static void main(String[] args) {
		new ChatClient1();
	}
}



