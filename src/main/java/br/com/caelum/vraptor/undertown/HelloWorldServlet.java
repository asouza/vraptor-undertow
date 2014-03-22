package br.com.caelum.vraptor.undertown;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.ListenerInfo;

import java.io.File;
import java.util.HashMap;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletException;

import org.apache.jasper.deploy.JspPropertyGroup;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.jboss.weld.environment.servlet.Listener;

import br.com.caelum.vraptor.VRaptor;
import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;

public class HelloWorldServlet {

	public static final String MYAPP = "/test";

	public static void main(final String[] args) {
		try {
			ListenerInfo weldListener = new ListenerInfo(Listener.class);
			ListenerInfo jspFactoryListener = new ListenerInfo(ConfigJspFactoryListener.class);
			InstanceFactory<? extends Filter> vraptorFilterFactory = new VRaptorFilterFactory();
			FilterInfo vraptorFilter = new FilterInfo("vraptor", VRaptor.class, vraptorFilterFactory);
			ResourceManager webappResourceManager = new FileResourceManager(new File("src/main/webapp"), 1000000l);			
			DeploymentInfo servletBuilder = deployment().setClassLoader(VRaptorFilterFactory.class.getClassLoader())
					.setContextPath(MYAPP).setDeploymentName("test.war")
					.addListener(weldListener)
					.addListener(jspFactoryListener)
					.addFilter(vraptorFilter)
					.addFilterUrlMapping("vraptor", "/*", DispatcherType.REQUEST)
					.addFilterUrlMapping("vraptor", "/*", DispatcherType.FORWARD)
					.addServlet(CDIAwareJspServletBuilder.createServlet("jsp"))
					.setResourceManager(webappResourceManager);
			CDIAwareJspServletBuilder.setupDeployment(servletBuilder, new HashMap<String, JspPropertyGroup>(),
					new HashMap<String, TagLibraryInfo>());
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
