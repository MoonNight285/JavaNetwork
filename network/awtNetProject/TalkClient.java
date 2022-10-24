package awtNetProject;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TalkClient extends MFrame implements ActionListener, Runnable{
	
	Button saveBtn, sendBtn;
	TextField sendTf;
	TextArea ta;
	Panel p1, p2;
	BufferedReader in;
	PrintWriter out;
	String id;
	String title;
	
	public TalkClient(BufferedReader in, PrintWriter out, String id) {
		super(350,400);
		title = "Talk 1.0 - " + id + "님 반갑습니다.";
		setTitle(title);
		p1 = new Panel();
		p1.setBackground(new Color(243, 97, 220));
		p1.add(saveBtn = new Button("SAVE"));
		
		p2 = new Panel();
		p2.setBackground(new Color(243, 97, 220));
		p2.add(new Label("CHAT ",Label.CENTER));
		p2.add(sendTf = new TextField("",25));
		p2.add(sendBtn = new Button("SEND"));	
		
		sendTf.addActionListener(this);
		saveBtn.addActionListener(this);
		sendBtn.addActionListener(this);
		
		add(p1,BorderLayout.NORTH);
		add(ta=new TextArea());
		add(p2,BorderLayout.SOUTH);
		validate();//갱신
		
		this.in = in;
		this.out = out;
		this.id = id;
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public boolean filterMgr(String target) {
		String filterList[] = {"바보","개새끼","새끼","자바","java"};
		
		boolean flag = false;
		for(int i = 0; i < filterList.length; ++i) {	
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
		if(obj == saveBtn) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter("./awtNetProject/chatLog" + 
						System.currentTimeMillis() + ".txt"));
				bw.write(ta.getText());
				bw.flush();
			} catch(IOException ex) {
				new MDialog(this, "에러", "파일 쓰기에 실패했습니다.");
			} finally {
				try {
					if(bw != null) {
						bw.close();
					}
				} catch(IOException ex2) {
					new MDialog(this, "에러", "파일을 쓰고 닫는과정에서 문제가 발생했습니다.");
				}
			}
			
			new MDialog(this, "Save", "대화내용을 저장하였습니다.");
		} else if(obj == sendTf || obj == sendBtn) {
			String str = sendTf.getText().trim();
			if(str.length() == 0) {
				return; // 메소드 종료
			}
			if(filterMgr(str)) { // 필터링 실행
				new MDialog(this, "경고", "입력하신 글자는 금지어입니다.");
				sendTf.setText("");
				sendTf.requestFocus();
				return;
			}
			
			sendMessage("CHATALL:" + "[" + id + "]" + str);
			sendTf.setText("");
			sendTf.requestFocus();
		}
	}//--actionPerformed
	
	public void sendMessage(String msg) {
		out.println(msg);
	}
	
	@Override
	//서버로 부터 메세지가 들어오면 반응하는 기능
	public void run() {
		try {
			while(true) {
				// 서버에서 메세지 전달되면 ta에 append
				ta.append(in.readLine() + "\n");
				sendTf.requestFocus(); // 채팅 입력창
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//--run
	
	class MDialog extends Dialog implements ActionListener{
		
		Button ok;
		TalkClient ct2;
		
		public MDialog(TalkClient ct2,String title, String msg) {
			super(ct2, title, true);
			this.ct2 = ct2;
			 //////////////////////////////////////////////////////////////////////////////////////////
			   addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			     dispose();
			    }
			   });
			   /////////////////////////////////////////////////////////////////////////////////////////
			   setLayout(new GridLayout(2,1));
			   Label label = new Label(msg, Label.CENTER);
			   add(label);
			   add(ok = new Button("확인"));
			   ok.addActionListener(this);
			   layset();
			   setVisible(true);
			   validate();
		}
		
		public void layset() {
			int x = ct2.getX();
			int y = ct2.getY();
			int w = ct2.getWidth();
			int h = ct2.getHeight();
			int w1 = 150;
			int h1 = 100;
			setBounds(x + w / 2 - w1 / 2, 
					y + h / 2 - h1 / 2, 200, 100);
		}

		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			
			if(obj == ok) {
				dispose();
			}
		}
	}
}



