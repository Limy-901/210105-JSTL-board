<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*, mvc.domain.Board, board.mvc.vo.ListResult"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<meta charset='utf-8'>
<style>
table, th, td {
border: 1px solid black;
border-collapse: collapse;
}
th, td {
padding: 5px;
}
a { text-decoration:none }
</style>
</head>
<body>
	<tr>
	<center>
		<td>번호</td>
		<td>이름</td>
		<td>이메일</td>
		<td>번호</td>
		</center>
	</tr>
	<c:choose>
		<c:when test="${empty login eq null}">
		<tr>로그인 하지 않았습니다.</tr>
		</c:when>
	</c:choose>	
<CENTER>
		<font color='gray' size='4' face='휴먼편지체'/>
			<hr width='600' size='2' color='gray' noshade>
			<h3> BOARD in Model 2 with JSTL </h3>
			<font color='gray' size='4' face='휴먼편지체'>
			<a href='../'>인덱스</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href='board.do?m=input'>글쓰기</a><br/>
		</font>
		<hr width='600' size='2' color='gray' noshade>
		<TABLE border='2' width='600' align='center' noshade>
		<TR size='2' align='center' noshade bgcolor='AliceBlue'>
			<th bgcolor='AliceBlue'>번호</th>
			<th align='center' width='10%'>작성자</th>
			<th align='center' width='30%'>이메일</th>
			<th align='center' width='30%'>글제목</th>
			<th align='center' width='15%'>작성일</th>
		</TR>
	
	<c:if test="${empty listResult.list}">
				<TR>
					<td align='center' colspan='5'>데이터가 없습니다.</td>
				</TR>
	</c:if>
	<c:forEach items="${listResult.list}" var="board">
	<TR>
					<TD>${board.seq}</TD>
					<TD>${board.writer}</TD>
					<TD>${board.email}</TD>
					<TD align='center'><a href='board.do?m=content&seq=${board.seq}'>${board.content}</a></TD>
					<TD align='center'>${board.rdate}</TD>
				</TR>
	</c:forEach>			
	</TABLE>
		<hr width='600' size='2' color='gray' noshade>
		<font color='gray' size='3' face='휴먼편지체'>
	    (총페이지수 : ${listResult.totalPageCount})
	    &nbsp;&nbsp;&nbsp;    
	    
	<c:forEach begin="1" end="${listResult.totalPageCount}" var="i">
		<a href="board.do?cp=${i}">
			<c:choose>
			   <c:when test="${i==listResult.currentPage}">
			   		<strong>${i}</strong>
			   </c:when>
				<c:otherwise>
					${i}
				</c:otherwise>
			</c:choose>
		</a>&nbsp;
	</c:forEach>
	    ( TOTAL : ${listResult.totalCount})
	    
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       페이지 싸이즈 : 
	       
	 	<select id="psId" name="ps" onchange="f(this)">
	<c:choose>
		   <c:when test="${listResult.pageSize==3}">
		   		<option value="3" selected>3</option>
				 <option value="5">5</option>
				<option value="10">10</option>
		   </c:when>
		   <c:when test="${listResult.pageSize==5}">
				 <option value="3">3</option>
				 <option value="5" selected>5</option>
				<option value="10">10</option>
		   </c:when>
		<c:otherwise>
			 <option value="3">3</option>
			<option value="5">5</option>
			<option value="10" selected>10</option>
		</c:otherwise>
		</c:choose>
	    </select>
	    <script language="javascript">
	       function f(select){
	           var el = document.getElementById("psId");
	           var ps = select.value;
	           alert("ps : " + ps);
	           location.href="board.do?m=list&ps="+ps;
	       }
	    </script>
	</font>
	<hr width='600' size='2' color='gray' noshade>
	</center>
	</body>
</html>
