package ch13;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamEx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InputStream in = System.in; // Ű����κ��� �Է¹���
		try {
			// throws �޼ҵ�� �ݵ�� ����ó���� �ؾ��Ѵ�.
			while(true) {
				int i = in.read(); // Ű���� �Է� ������ �����·� ����(Ű���� �Է½� ����)
				if(i == -1) break;
				if(i == '\n') System.out.println();
				else System.out.print((char)i);
			}
		} catch (IOException e) {
			e.printStackTrace(); // ���ܰ� �Ͼ�� ��� �� �޼��� ���
		}
	}

}
