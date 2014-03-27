<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="tagfile"%>
<html>
	<body>
		<%@include file="header.jsp"%>
		<jsp:useBean id="helper2" class="br.com.caelum.vraptor.undertown.Helper"/>
		<tagfile:test text="${message}"/>		
		${helper.doStuff()}<br/>
		${helper2.doStuff()}
		<c:forEach var="w" items="${helper.words()}">
			Pilot : ${w}<br/>
		</c:forEach>
	</body>
</html>