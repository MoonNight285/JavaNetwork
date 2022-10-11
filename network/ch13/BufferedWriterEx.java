package ch13;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class BufferedWriterEx {
	public static void main(String[] args) {
		String s = "������ ��ſ� ȭ�����Դϴ�.";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			bw.write(s);
			bw.flush();
			bw.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
}
