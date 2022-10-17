package ch14;

public class ChatProtocol2 {
	
	// (C -> S) ID:aaa 이렇게 보내줄것
	// (S -> C) CHALLIST:aaa;bbb;ccc;강호동; 접속자 명단을 이렇게 보내준다.
	public static final String ID = "ID";
	
	// (C -> S) CHAT:받는아이디;메세지 EX)CHAT:bbb;밥먹자 => 이런식으로 귓속말 대화를 보내준다.
	// (S -> C) CHAT:보내는아이디;메세지 EX)CHAT:aaa;밥먹자 => 위와 동일
	public static final String CHAT = "CHAT";
	
	// (C -> S) CHATALL:메세지 => 모든 대상에게 메세지를 보낼때
	// (S -> C) CHALALL:[보낸아이디]메세지
	public static final String CHATALL = "CHATALL";
	
	// (C -> S) MESSAGE:받는아이디;쪽지내용 EX)MESSAGE:bbb;지금 어디?
	// (S -> C) MESSAGE:보내는아이디;쪽지내용 EX)MESSAGE:aaa;지금 어디?
	public static final String MESSAGE = "MESSAGE";
	
	// (S -> C) CHALLIST:aaa;bbb;ccc;강호동;
	public static final String CHALLIST = "CHALLIST";
	
	//  구분자 -> 프로토콜:data(delimiter)
	public static final String DM = ";";
}
