package br.com.caelum.vraptor.undertown;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.servlet;

import javax.servlet.ServletException;

import org.jboss.weld.environment.servlet.Listener;

public class HelloWorldServlet {

	public static final String MYAPP = "/myapp";

	public static void main(final String[] args) {
		try {

			ListenerInfo weldListener = new ListenerInfo(Listener.class);
			DeploymentInfo servletBuilder = deployment().setClassLoader(HelloServlet.class.getClassLoader())
					.setContextPath(MYAPP).setDeploymentName("test.war")
					.addServlets(servlet("HelloServlet", HelloServlet.class).addMapping("/hello"))
					.addListener(weldListener);

			DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
			manager.deploy();

			HttpHandler servletHandler = manager.start();
			PathHandler path = Handlers.path(Handlers.redirect(MYAPP)).addPrefixPath(MYAPP, servletHandler);
			Undertow server = Undertow.builder().addHttpListener(8080, "localhost").setHandler(path).build();
			server.start();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}
}
