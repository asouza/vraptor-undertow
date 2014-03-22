package br.com.caelum.vraptor.undertown;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;

import java.util.HashMap;

import javax.enterprise.inject.spi.CDI;

import org.apache.jasper.deploy.JspPropertyGroup;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.apache.jasper.servlet.JspServlet;
import org.apache.tomcat.InstanceManager;
import org.jboss.weld.manager.BeanManagerImpl;

import static org.apache.jasper.Constants.JSP_PROPERTY_GROUPS;
import static org.apache.jasper.Constants.JSP_TAG_LIBRARIES;
import static org.apache.jasper.Constants.SERVLET_VERSION;

public class CDIAwareJspServletBuilder {

	public static void setupDeployment(final DeploymentInfo deploymentInfo,
			final HashMap<String, JspPropertyGroup> propertyGroups, final HashMap<String, TagLibraryInfo> tagLibraries) {
		deploymentInfo.addServletContextAttribute(SERVLET_VERSION, deploymentInfo.getMajorVersion() + "."
				+ deploymentInfo.getMinorVersion());
		deploymentInfo.addServletContextAttribute(JSP_PROPERTY_GROUPS, propertyGroups);
		deploymentInfo.addServletContextAttribute(JSP_TAG_LIBRARIES, tagLibraries);
		deploymentInfo.addServletContextAttribute(InstanceManager.class.getName(),new WeldInstanceManagerHackWrapper());
	}

	public static ServletInfo createServlet(final String name) {
		ServletInfo jspServlet = new ServletInfo(name, JspServlet.class);
		jspServlet.addMapping("*.jsp").addMapping("*.jspx");
		// if the JSP servlet is mapped to a path that ends in /*
		// we want to perform welcome file matches if the directory is requested
		jspServlet.setRequireWelcomeFileMapping(true);
		return jspServlet;
	}

}
