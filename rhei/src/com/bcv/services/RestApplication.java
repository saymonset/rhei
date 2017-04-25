/**
 * 
 */
package com.bcv.services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.packtpub.wflydevelopment.chapter7.boundary.SeatsResource;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 30/10/2015 08:46:53 2015 mail :
 *         oraclefedora@gmail.com
 */
@ApplicationPath("/services")
public class RestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public RestApplication() {
		singletons.add(new CustomerResource());
		singletons.add(new ConsultaServicio());
	//	singletons.add(new FacturaServicio());
		singletons.add(new ValidNuRefPagoServicio());
		singletons.add(new SeatsResource());
		empty.add(SeatsResource.class);

	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}