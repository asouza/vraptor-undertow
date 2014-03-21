package br.com.caelum.vraptor.undertown;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.ListenerInfo;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletException;

import org.jboss.weld.environment.servlet.Listener;

import br.com.caelum.vraptor.VRaptor;
import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;

public class HelloWorldServlet {

	public static final String MYAPP = "/myapp";

	public static void main(final String[] args) {
		try {

			ListenerInfo weldListener = new ListenerInfo(Listener.class);
			InstanceFactory<? extends Filter> vraptorFilterFactory = new VRaptorFilterFactory();
			;
			FilterInfo vraptorFilter = new FilterInfo("vraptor", VRaptor.class, vraptorFilterFactory);			
			DeploymentInfo servletBuilder = deployment().setClassLoader(VRaptorFilterFactory.class.getClassLoader())
					.setContextPath(MYAPP).setDeploymentName("test.war").addListener(weldListener)
					.addFilter(vraptorFilter).addFilterUrlMapping("vraptor", "/*", DispatcherType.REQUEST);

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
