package ch14;

public class ChatProtocol2 {
	
	// (C -> S) ID:aaa �̷��� �����ٰ�
	// (S -> C) CHALLIST:aaa;bbb;ccc;��ȣ��; ������ ����� �̷��� �����ش�.
	public static final String ID = "ID";
	
	// (C -> S) CHAT:�޴¾��̵�;�޼��� EX)CHAT:bbb;����� => �̷������� �ӼӸ� ��ȭ�� �����ش�.
	// (S -> C) CHAT:�����¾��̵�;�޼��� EX)CHAT:aaa;����� => ���� ����
	public static final String CHAT = "CHAT";
	
	// (C -> S) CHATALL:�޼��� => ��� ��󿡰� �޼����� ������
	// (S -> C) CHALALL:[�������̵�]�޼���
	public static final String CHATALL = "CHATALL";
	
	// (C -> S) MESSAGE:�޴¾��̵�;�������� EX)MESSAGE:bbb;���� ���?
	// (S -> C) MESSAGE:�����¾��̵�;�������� EX)MESSAGE:aaa;���� ���?
	public static final String MESSAGE = "MESSAGE";
	
	// (S -> C) CHALLIST:aaa;bbb;ccc;��ȣ��;
	public static final String CHALLIST = "CHALLIST";
	
	//  ������ -> ��������:data(delimiter)
	public static final String DM = ";";
}
