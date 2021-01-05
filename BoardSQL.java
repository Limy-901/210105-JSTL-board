package board.mvc.model;

class BoardSQL {
	static final String LIST = "select * from (select ROWNUM rnum, aa.* "
			+ "from (select * from BOARD order "
			+ "by SEQ desc) aa ) where rnum>? and rnum<=?";
	static final String INSERT = "insert into BOARD values(BOARD_SEQ.nextval, "
			+ "?, ?, ?, ?, SYSDATE)";
	static final String CONTENT = "select * from BOARD where SEQ=?";
	static final String DELETE = "delete from BOARD where SEQ=?";
	static final String UPDATE = "update BOARD set EMAIL=?, SUBJECT=?, CONTENT=? where SEQ=?"; 
	static final String TOTAL = "select count(*) FROM BOARD";
	static final String LIST_PAGE ="select * from (select ROWNUM rnum, aa.* from (select * from BOARD order by SEQ desc) aa ) where rnum>? and rnum<=?";
	static final String COUNT = "select max(ROWNUM) from BOARD";
}
	
	// 또는, select count(SEQ) from BOARD;
	//개수 구할때, 로우넘의 최대
