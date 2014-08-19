package br.com.caelum.vraptor.undertown;

import org.jboss.modules.ModuleLoadException;

import br.com.caelum.vraptor.undertown.builder.ServerBuilder;

public class StartServerExample {

	public static void main(final String[] args) throws ModuleLoadException {
		ServerBuilder.root()
			.webAppFolder("webapp")
			.warName("musicjungle")
			.port(8080)
			.address("localhost")
			.addInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext", "messages")
			.start();
	}

}
