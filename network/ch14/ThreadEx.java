package ch14;

class MyThread extends Thread {
	String name;
	
	public MyThread(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		try {
			for(int i = 0; i < 10; ++i) {
				System.out.println(name + " : " + i);
				Thread.sleep(500); // 0.5초 대기
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

public class ThreadEx {
	public static void main(String[] args) {
		MyThread m1 = new MyThread("첫번째");
		MyThread m2 = new MyThread("두번째");
		
		m1.start();
		m2.start();
	}
}
