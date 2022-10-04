package ch13;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamEx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InputStream in = System.in; // 키보드로부터 입력받음
		try {
			// throws 메소드는 반드시 예외처리를 해야한다.
			while(true) {
				int i = in.read(); // 키보드 입력 전까지 대기상태로 존재(키보드 입력시 실행)
				if(i == -1) break;
				if(i == '\n') System.out.println();
				else System.out.print((char)i);
			}
		} catch (IOException e) {
			e.printStackTrace(); // 예외가 일어나는 경로 및 메세지 출력
		}
	}

}
