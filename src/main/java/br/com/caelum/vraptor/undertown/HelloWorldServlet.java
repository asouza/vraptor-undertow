package br.com.caelum.vraptor.undertown;

import br.com.caelum.vraptor.undertown.builder.ServerBuilder;

public class HelloWorldServlet {

	public static void main(final String[] args) {
		ServerBuilder.context("/app").webAppFolder("src/main/webapp").warName("musicjungle").port(8080)
				.address("localhost").start();
	}

}
