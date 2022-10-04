package ch13;

import java.io.FileReader;

// java 패키지명.클래스명
// java ch13.FileReaderEx에서 실행
public class FileReaderEx {

	public static void main(String[] args) {
		try {
			FileReader fr = new FileReader("ch13/song.txt");
			int i;
			
			// 파일의 마지막에 있는 값이 -1
			while((i = fr.read()) != -1) {
				System.out.println((char)i);
			}
			fr.close();
			System.out.println("\nEND~~");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
