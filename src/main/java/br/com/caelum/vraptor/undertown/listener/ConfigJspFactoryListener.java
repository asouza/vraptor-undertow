package br.com.caelum.vraptor.undertown.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspFactory;

public class ConfigJspFactoryListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		JspFactory.setDefaultFactory(new org.apache.jasper.runtime.JspFactoryImpl());
	}

}
