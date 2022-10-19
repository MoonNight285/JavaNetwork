package ch14;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChatMgr3 {

	private DBConnectionMgr pool;
	
	public ChatMgr3() {
		pool = DBConnectionMgr.getInstance();
	}
	
	//로그인 
	public boolean loginChk(String id, String pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();//Connection 빌려옴
			sql = "select id from tblRegister where id=? and pwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);//첫번째 ?에 id = 'aaa'
			pstmt.setString(2, pwd);//두번째 ?에 pwd = '1234'
			rs = pstmt.executeQuery();//실행문
			flag = rs.next();//조건에 맞는 결과값이 있으면 true 없으면 false
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//con은 반납, pstmt, rs 둘다 close
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}
	
}









