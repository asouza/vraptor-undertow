package br.com.caelum.vraptor.undertown.hack;

import io.undertow.jsp.HackInstanceManager;

import java.lang.reflect.InvocationTargetException;

import javax.enterprise.inject.spi.CDI;
import javax.naming.NamingException;

import org.apache.tomcat.InstanceManager;
import org.jboss.weld.manager.BeanManagerImpl;

/**
 * {@link InstanceManager} hack to create and delegate instances to CDI container.
 * @author Alberto Souza
 *
 */
public class WeldInstanceManagerHackWrapper implements InstanceManager {

	private WeldInstanceManagerHack delegate;
	private HackInstanceManager hackInstanceManager = new HackInstanceManager();

	@Override
	public void destroyInstance(Object instance) throws IllegalAccessException, InvocationTargetException {
		hackInstanceManager.destroyInstance(instance);
		initialize();
		delegate.destroyInstance(instance);
	}

	@Override
	public Object newInstance(String fqcn) throws IllegalAccessException, InvocationTargetException, NamingException,
			InstantiationException, ClassNotFoundException {
		Object newInstance = hackInstanceManager.newInstance(fqcn);
		initialize();
		delegate.newInstance(newInstance);
		return newInstance;
	}

	@Override
	public Object newInstance(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, NamingException,
			InstantiationException {
		Object newInstance = hackInstanceManager.newInstance(clazz);
		initialize();
		delegate.newInstance(newInstance);
		return newInstance;
	}

	@Override
	public void newInstance(Object object) throws IllegalAccessException, InvocationTargetException, NamingException {
		hackInstanceManager.newInstance(object);
		initialize();
		delegate.newInstance(object);
	}

	@Override
	public Object newInstance(String fqcn, ClassLoader classLoader) throws IllegalAccessException, InvocationTargetException,
			NamingException, InstantiationException, ClassNotFoundException {
		Object newInstance = hackInstanceManager.newInstance(fqcn,classLoader);
		initialize();
		delegate.newInstance(fqcn, classLoader);
		return newInstance;
	}

	private void initialize(Object... args) {
		
		if (delegate == null) {
			BeanManagerImpl weldManager = CDI.current().select(BeanManagerImpl.class).get();
			this.delegate = new WeldInstanceManagerHack(weldManager);
		}

	}

}
