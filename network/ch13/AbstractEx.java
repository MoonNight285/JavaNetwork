package ch13;

import java.awt.Button;
import java.awt.Color;
import java.awt.Label;

abstract class Shape {
	abstract void draw(); // 추상메소드 : 몸체는 없고 선언만 있음.
}

class Rectangle extends Shape {
	@Override
	void draw() {
		
	}
}

class Circle extends Shape {
	@Override
	void draw() {
		
	}
}

public class AbstractEx {
	public AbstractEx() {
		super(); // 상위클래스 생성자(Object)
	}
	
	public static void main(String[] args) {
		AbstractEx ae = new AbstractEx();
		Button btn = new Button("버튼");
		btn.setBackground(Color.RED);
		Label label = new Label("라벨");
		
		try {
			int arr[] = new int[3];
			arr[3] = 22;
		} catch(Exception ex) {
			System.out.println("배열예외");
		}
	}
}
