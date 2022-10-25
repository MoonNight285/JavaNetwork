package ch14;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class MsgMgr4 {
	private DBConnectionMgr pool;
	
	public MsgMgr4() {
		pool = DBConnectionMgr.getInstance();
	}
	
	// insert
	public void insertMsg(MessageBean4 bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			con = pool.getConnection();//Connection 빌려옴
			sql = "insert tblMessage(fid, tid, msg) values(?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getFid());
			pstmt.setString(2, bean.getTid());
			pstmt.setString(3, bean.getMsg());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
	}
	
	// list
	public Vector<MessageBean4> getMsgList(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<MessageBean4> vlist = new Vector<MessageBean4>();
		try {
			con = pool.getConnection();//Connection 빌려옴
			sql = "select * from tblMessage where fid = ? or tid = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MessageBean4 bean = new MessageBean4();
				bean.setNo(rs.getInt("no"));
				bean.setFid(rs.getString("fid"));
				bean.setTid("tid");
				bean.setMsg("msg");
				bean.setMdate(rs.getString("mdate")); // 날짜타입도 문자타입으로 리턴가능
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return vlist;
	}
	
	// get : select * from tblMessage where no = ?
	public MessageBean4 getMsg(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		MessageBean4 bean = new MessageBean4();
		try {
			con = pool.getConnection();//Connection 빌려옴
			sql = "select * from tblMessage where no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			
			if(rs.next() == true) {
				bean.setNo(rs.getInt("no"));
				bean.setFid(rs.getString("fid"));
				bean.setTid("tid");
				bean.setMsg("msg");
				bean.setMdate(rs.getString("mdate")); // 날짜타입도 문자타입으로 리턴가능
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return bean;
	}
}
