package br.com.caelum.vraptor.undertown;

import javax.enterprise.inject.Vetoed;

import org.jboss.weld.environment.tomcat7.WeldInstanceManager;
import org.jboss.weld.manager.api.WeldManager;

/**
 * Hack to {@link WeldInstanceManager}. Default visibility is protected.
 * @author Alberto Souza
 *
 */
@Vetoed
public class WeldInstanceManagerHack extends WeldInstanceManager{

	public  WeldInstanceManagerHack(WeldManager manager) {
		super(manager);
	}

}
