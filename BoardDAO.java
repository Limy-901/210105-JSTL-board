package board.mvc.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import mvc.domain.Board;

import static board.mvc.model.BoardSQL.*;

class BoardDAO {
	private DataSource ds;
	
	BoardDAO(){
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
		}catch(NamingException ne) {}
	}
	Board content(long seq) {
		String sql = CONTENT;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, seq);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String writer = rs.getString(2);
				String email = rs.getString(3);
				String subject = rs.getString(4);
				String content = rs.getString(5);
				Date rdate = rs.getDate(6);
				Board dto = new Board(seq,writer,email,subject,content,rdate);
				return dto;
			}
		}catch(SQLException se) {
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException sse) {}
		}
		return null;
	}
	
	ArrayList<Board> list(){
		ArrayList<Board> dtos = new ArrayList<Board>();
		String sql = LIST;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				int rownum = rs.getInt(1); 
				long seq = rs.getLong(2);
				String writer = rs.getString(3);
				String email = rs.getString(4);
				String subject = rs.getString(5);
				String content = rs.getString(6);
				Date rdate = rs.getDate(7);
				dtos.add(new Board(seq,writer,email,subject,content,rdate));
			}
		}catch(SQLException se) {
			System.out.println("se : "+se);
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();
			}catch(SQLException sse) {}
		}
		return dtos;
	}
	ArrayList<Board> list(int cp,int ps){ // 리스트 메소드를 오버로딩함.
		// 이 cp와 ps는 어떻게 받을것인가?
		ArrayList<Board> dtos = new ArrayList<Board>();
		String sql = LIST_PAGE;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			int startRow = (cp-1)*ps; //이것들은 어떻게 구하나? 0, 3, 6, ...
			int endRow = cp*ps; // 현재 페이지 x 페이지 사이즈 ( 3번째글 x 3번째페이지 = 9번째글이됨 ) 3, 6, 9, ... 
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				int rownum = rs.getInt(1); 
				Long seq = rs.getLong(2);
				String writer = rs.getString(3);
				String email = rs.getString(4);
				String subject = rs.getString(5);
				String content = rs.getString(6);
				Date rdate = rs.getDate(7);
				dtos.add(new Board(seq,writer,email,subject,content,rdate));
			}
		}catch(SQLException se) {
			System.out.println("se : "+se);
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();
			}catch(SQLException sse) {}
		}
		return dtos;
	}
	
	boolean del(Long seq) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = DELETE;
		try {
			con=ds.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setLong(1, seq);
			int i =pstmt.executeUpdate();
			if(i>0) return true;
			else return false;
		}catch(SQLException se) {
			return false;
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();
			}catch(SQLException sse) {}
		}
	}
	void insert(Board dto) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = INSERT;
		try {
			con=ds.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, dto.getWriter());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.executeUpdate();
		}catch(SQLException se) {
			
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();
			}catch(SQLException sse) {}
		}
	}
	void DBupdate(Board board) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = UPDATE;
		try {
			con=ds.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, board.getEmail());
			pstmt.setString(2, board.getSubject());
			pstmt.setString(3, board.getContent());
			pstmt.setLong(4, board.getSeq());
			pstmt.executeUpdate();
		}catch(SQLException se) {
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();
			}catch(SQLException sse) {}
		}
	}
	long totalCount() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = TOTAL;
		long count = -1;
		try {
			con=ds.getConnection();
			stmt=con.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				count = rs.getLong(1);
				return count;
			}else {
				return -1;
			}
		}catch(SQLException se) {
			System.out.println("cound cp, ps se:"+se);
			return -1;
		}finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(con!=null) con.close();
			}catch(SQLException sse) {}
		}
	}
}
