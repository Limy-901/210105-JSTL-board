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
				System.out.println("����Ʈ ȣ��");
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
		// ����¡������ session�� set, get���� ���� ����.
		// (1) Current Page : cp
		int cp = 1; //�ϴ� cp�� 1�̶�� ����
		String cpStr = request.getParameter("cp"); //get������� �Ѿ�� cp�� ps�� �޾ƿ�.
		String psStr = request.getParameter("ps"); // board.do?m=list&cp=?&ps=? 

		HttpSession session = request.getSession(true);//������ �����´�.
		
		if(cpStr == null) {// ���� �Ķ���ͷ� �Է� ���� ���� ������,
			Object cpObj = session.getAttribute("cp");// ���� ���ǿ� �ԷµǾ��ִ� cp���� �����ͼ� cpObj �� ����.
			if(cpObj != null) cp=(Integer)cpObj;// ���ǿ� �ִ� ����, int�� ����ȯ�ؼ� cp�� ������.
		}else {// cpStr�� �Էµ� ���� ������,
			cpStr = cpStr.trim();// ������ �����ؼ�
			cp = Integer.parseInt(cpStr);// int�� ����ȯ�ؼ� cp�� ������.
		}
		session.setAttribute("cp", cp);// �� ������ ���� cp(����������)�� �������� ������ ����ص�.
		
		// (2) Page Size : ps 
		int ps = 1;// 
		if(psStr == null) {// �Ķ���ͷ� ���� ���� ������,
			Object psObj = session.getAttribute("ps"); // ���ǿ� �ִ� ps���� �����ͼ� psObj�� �� ����.
			if(psObj != null) ps=(Integer)psObj;// �� ���� ���� �ƴϸ�, // ��Ʈ�� ����ȯ�ؼ� ps�� ����
		}else {
			psStr = psStr.trim();// ������ �����ؼ� ����
			int psParam = Integer.parseInt(psStr);// ����ȯ�ؼ� psParam���� ����
			Object psObj = session.getAttribute("ps");
			if(psObj != null) { //���ǿ� ��ϵ� ���� �ִٸ�,
				int psSession = (Integer)psObj; // �� ���� �����ͼ�, int�� ����ȯ �� psSession�� ����
				if(psSession != psParam) { // ���ǿ� �ִ� ����, �Ķ���ͷ� �Ѿ�� ���� �ٸ���,
					//ps�� ���� cp�� �ٲ��.
					cp = 1; // ���� �������� 1�� ���ְ�,
					session.setAttribute("cp", cp); // 1�������� ���ǿ� �����.
				}
			}else {
				if(ps != psParam) {//�Ķ���ͷ� �Ѿ�°��� ps�� ���� ������,
					cp = 1; // ���� �������� 1�� ���ְ�,
					session.setAttribute("cp", cp); // 1�������� ���ǿ� �����.
				}
			}
			ps = psParam; //�Ķ���Ϳ��� �Ѿ�� ������ ps�� ��������.
		}
		session.setAttribute("ps", ps); // ������ ps���� ���ǿ� �����.
		
		BoardService service = BoardService.getInstance();//���� ��ü ȣ��
		ListResult listResult = service.getListResult(cp, ps);//cp��ps�� ���ο� �ٸ���Ʈ����Ʈ��ü �������� 
		request.setAttribute("listResult", listResult); //������ ��ü�� request�� ���.
		
		if(listResult.getList().size() == 0 && cp>1) { //�ش� ����������Ʈ�� �����, �������� 1������ �̻��� ���
			response.sendRedirect("board.do?m=list&cp="+(cp-1)); // (������Ƿ�) ���� �������� �̵�
		}else { //�ش� �������� ���������� �����Ѵٸ�
			String view = "list.jsp"; //����Ʈ�� �̵�(�����͸� ������)
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
