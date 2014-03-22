package br.com.caelum.vraptor.undertown;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;

@Controller
@Named("component")
public class Component {

	@Inject
	private Result result;

	public void test() {
		result.include("message","mensagem");
	}

	public void message() {
		System.out.println("message");
	}
}
