package br.com.caelum.vraptor.undertown;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class Helper {

	public String doStuff(){
		return "doing";
	}
	
	public String[] words(){
		return new String[]{"senna","prost","alonso","vettel"};
	}
}
