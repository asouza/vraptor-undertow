package br.com.caelum.vraptor.undertown;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class Component {

	public String get(){
		return "opaopa";
	}
	
	@PreDestroy
	public void killing(){
		System.out.println("matando meu component");
	}
}
