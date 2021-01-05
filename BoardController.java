package board.mvc.control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.mvc.model.BoardService;
import board.mvc.vo.ListResult;
import mvc.domain.Board;


@WebServlet("/board/board.do")

public class BoardController extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		String m = request.getParameter("m");
		if(m!=null) {
			m = m.trim();
			if(m.equals("list")) {
				list(request,response);
			}else if(m.equals("input")) {
				input(request,response);
			}else if(m.equals("insert")) {
				insert(request,response);
			}else if(m.equals("del")) {
				del(request,response);
			}else if(m.equals("content")) {
				System.out.println("콘텐트 호출");
				content(request,response);
			}else if(m.equals("update")){
				update(request,response);
			}else if(m.equals("DBupdate")) {
				System.out.println("m:DBupdate");
				DBupdate(request,response);
			}
		}else {
			list(request,response);
		}
	}
	private void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 페이징에서는 session에 set, get으로 값을 설정.
		// (1) Current Page : cp
		int cp = 1; //일단 cp를 1이라고 설정
		String cpStr = request.getParameter("cp"); //get방식으로 넘어온 cp와 ps를 받아옴.
		String psStr = request.getParameter("ps"); // board.do?m=list&cp=?&ps=? 

		HttpSession session = request.getSession(true);//세션을 가져온다.
		
		if(cpStr == null) {// 따로 파라미터로 입력 들어온 값이 없으면,
			Object cpObj = session.getAttribute("cp");// 원래 세션에 입력되어있던 cp값을 가져와서 cpObj 로 만듦.
			if(cpObj != null) cp=(Integer)cpObj;// 세션에 있던 값을, int로 형변환해서 cp에 대입함.
		}else {// cpStr에 입력된 값이 있으면,
			cpStr = cpStr.trim();// 공백을 제거해서
			cp = Integer.parseInt(cpStr);// int로 형변환해서 cp에 대입함.
		}
		session.setAttribute("cp", cp);// 이 과정을 거진 cp(현재페이지)를 세션으로 값으로 기록해둠.
		
		// (2) Page Size : ps 
		int ps = 1;// 
		if(psStr == null) {// 파라미터로 들어온 값이 없으면,
			Object psObj = session.getAttribute("ps"); // 세션에 있는 ps값을 가져와서 psObj로 값 넣음.
			if(psObj != null) ps=(Integer)psObj;// 이 값이 널이 아니면, // 인트로 형변환해서 ps에 대입
		}else {
			psStr = psStr.trim();// 공백을 제거해서 대입
			int psParam = Integer.parseInt(psStr);// 형변환해서 psParam으로 저장
			Object psObj = session.getAttribute("ps");
			if(psObj != null) { //세션에 기록된 값이 있다면,
				int psSession = (Integer)psObj; // 그 값을 가져와서, int로 형변환 후 psSession에 대입
				if(psSession != psParam) { // 세션에 있던 값이, 파라미터로 넘어온 값과 다르면,
					//ps에 따라서 cp가 바뀐다.
					cp = 1; // 현재 페이지를 1로 해주고,
					session.setAttribute("cp", cp); // 1페이지를 세션에 기록함.
				}
			}else {
				if(ps != psParam) {//파라미터로 넘어온값과 ps가 같지 않으면,
					cp = 1; // 현재 페이지를 1로 해주고,
					session.setAttribute("cp", cp); // 1페이지를 세션에 기록함.
				}
			}
			ps = psParam; //파라미터에서 넘어온 값으로 ps를 지정해줌.
		}
		session.setAttribute("ps", ps); // 지정된 ps값을 세션에 기록함.
		
		BoardService service = BoardService.getInstance();//서비스 객체 호출
		ListResult listResult = service.getListResult(cp, ps);//cp와ps로 새로운 겟리스트리절트객체 생성해줌 
		request.setAttribute("listResult", listResult); //생성된 객체를 request에 기록.
		
		if(listResult.getList().size() == 0 && cp>1) { //해당 페이지리스트가 비었고, 페이지가 1페이지 이상인 경우
			response.sendRedirect("board.do?m=list&cp="+(cp-1)); // (비었으므로) 이전 페이지로 이동
		}else { //해당 페이지가 정상적으로 존재한다면
			String view = "list.jsp"; //리스트로 이동(데이터를 가지고)
			RequestDispatcher rd = request.getRequestDispatcher(view);
			rd.forward(request, response);
		}
	}
	
	private void input(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String view = "input.jsp";
		response.sendRedirect(view);
		
	}
	private void insert(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String writer = request.getParameter("writer");
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		Board dto = new Board(-1,writer,email,subject,content,null);
		BoardService service = BoardService.getInstance();
		service.insertS(dto);
		
		String view = "board.do";
		response.sendRedirect(view);
	}
	private void del(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String seqStr = request.getParameter("seq");
		int seq = -1;
		if(seqStr != null) {
			seqStr = seqStr.trim();
			try {
			    seq = Integer.parseInt(seqStr);
				BoardService service = BoardService.getInstance();
				boolean flag = service.delS(seq);
				request.setAttribute("flag", flag);
			}catch(NumberFormatException nfe) {}
		}
		String view = "msg.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(view);
		rd.forward(request, response);
		
	}
	private void content(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long seq = getSeq(request);
		//int seq = -1;
		if(seq!=-1L) {
			BoardService service = BoardService.getInstance();
			Board board=service.contentS(seq);
			request.setAttribute("board", board);
			
			String view="content.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(view);
			rd.forward(request, response);

		}else {
			String view = "board.do";
			response.sendRedirect(view);
		}
		
	}
	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String seqStr = request.getParameter("seq");
		int seq = -1;
		if(seqStr != null) {
			seqStr = seqStr.trim();
			try {
			    seq = Integer.parseInt(seqStr);
				BoardService service = BoardService.getInstance();
				Board board = service.contentS(seq);
				request.setAttribute("board",board);
				String view = "update.jsp";
				RequestDispatcher rd = request.getRequestDispatcher(view);
				rd.forward(request, response);	
			}catch(NumberFormatException nfe) {}
		}
	}
	private void DBupdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long seq = getSeq(request);
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		Board board = new Board(seq,null,email,subject,content,null);
		BoardService service = BoardService.getInstance();
		service.DBupdateS(board);
		
		String view = "board.do";
		response.sendRedirect(view);
	}
	private long getSeq(HttpServletRequest request) {
		String seqStr = request.getParameter("seq");
		long seq = 0L;
		if(seqStr == null) return -1L;
		else {
			seqStr = seqStr.trim();
			try {
				seq = Integer.parseInt(seqStr);
				return seq;
			}catch(NumberFormatException se) {
				return -1L;
			}
		}
	}
}
