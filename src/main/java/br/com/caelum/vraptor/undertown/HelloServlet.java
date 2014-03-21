package br.com.caelum.vraptor.undertown;

import java.io.IOException;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet{
	
	private Component component = CDI.current().select(Component.class).get();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().print("<html><body>"+component.get()+"</body></html>");
	}
}
