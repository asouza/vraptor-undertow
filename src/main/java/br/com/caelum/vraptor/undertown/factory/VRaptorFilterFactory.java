package br.com.caelum.vraptor.undertown.factory;

import javax.enterprise.inject.spi.CDI;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.ImmediateInstanceHandle;
import br.com.caelum.vraptor.VRaptor;

public class VRaptorFilterFactory implements InstanceFactory<VRaptor> {

	@Override
	public InstanceHandle<VRaptor> createInstance() throws InstantiationException {
		VRaptor vraptor = CDI.current().select(VRaptor.class).get();
		return new ImmediateInstanceHandle<>(vraptor);
	}

}
