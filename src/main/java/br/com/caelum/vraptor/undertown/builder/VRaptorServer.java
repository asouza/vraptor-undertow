package br.com.caelum.vraptor.undertown.builder;

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
import br.com.caelum.vraptor.undertown.factory.VRaptorFilterFactory;
import br.com.caelum.vraptor.undertown.hack.TldsLoader;
import br.com.caelum.vraptor.undertown.listener.ConfigJspFactoryListener;
import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;

class VRaptorServer {


	static void start(String context,String webAppFolder,String warName,int port,String address){
		try {
			ListenerInfo weldListener = new ListenerInfo(Listener.class);
			ListenerInfo jspFactoryListener = new ListenerInfo(ConfigJspFactoryListener.class);
			InstanceFactory<? extends Filter> vraptorFilterFactory = new VRaptorFilterFactory();
			FilterInfo vraptorFilter = new FilterInfo("vraptor", VRaptor.class, vraptorFilterFactory);
			//it would be nice to discover what this is
			long transferMinSize = 100l;
			ResourceManager webappResourceManager = new FileResourceManager(new File(webAppFolder), transferMinSize);
			DeploymentInfo webXml = deployment().setClassLoader(VRaptorFilterFactory.class.getClassLoader())
					.setContextPath(context).setDeploymentName(warName+".war")
					.addListener(jspFactoryListener)
					.addListener(weldListener)
					.addFilter(vraptorFilter)
					.addFilterUrlMapping("vraptor", "/*", DispatcherType.REQUEST)
					.addFilterUrlMapping("vraptor", "/*", DispatcherType.FORWARD)
					.addServlet(CDIAwareJspServletBuilder.createServlet("jsp"))
					.setResourceManager(webappResourceManager);
			
			
			CDIAwareJspServletBuilder.setupDeployment(webXml, new HashMap<String, JspPropertyGroup>(),
					TldsLoader.load());
			
			DeploymentManager manager = defaultContainer().addDeployment(webXml);
			manager.deploy();

			HttpHandler servletHandler = manager.start();
			PathHandler path = Handlers.path(Handlers.redirect(context)).addPrefixPath(context, servletHandler);
			
			Undertow server = Undertow.builder().addHttpListener(port, address).setHandler(path).build();
			server.start();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}
}
