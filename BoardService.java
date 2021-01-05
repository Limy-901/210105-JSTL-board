package board.mvc.model;

import java.util.ArrayList;
import java.util.List;

import board.mvc.vo.ListResult;
import mvc.domain.Board;

public class BoardService {
	
	ListResult lr = new ListResult();
	
	private BoardDAO dao;
	private static final BoardService instance = new BoardService();
	
	private BoardService() {
		dao = new BoardDAO();
	}
	
	public static BoardService getInstance() {
		return instance;
	}
	public void insertS(Board dto) {
		dao.insert(dto);
	}
	public boolean delS(long seq) {
		return dao.del(seq);
	}
	public Board contentS(long seq) {
		return dao.content(seq);
	}
	public void DBupdateS(Board board) {
		dao.DBupdate(board);
	}
	
	public long total() {
		return dao.totalCount();
	}
	public ListResult getListResult(int cp, int ps) {
		List<Board> list = dao.list(cp,ps);
		long totalCount = dao.totalCount();
		// 여러개의 dao를 가지고 해야함.
		ListResult r = new ListResult(cp, totalCount, ps, list);
		return r;
	}
	public ArrayList<Board> listS(int cp, int ps){
		return dao.list(cp, ps);
	}
}
