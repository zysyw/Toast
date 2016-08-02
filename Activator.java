package org.equinoxosgi.toast.internal.clint.emergency.bundle;

import javax.naming.Context;

import org.equinoxosgi.toast.dev.airbag.IAirbag;
import org.equinoxosgi.toast.dev.gps.IGps;
import org.equinoxosgi.toast.internal.clint.emergency.EmergencyMonitor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {
	private IGps gps;
	private ServiceReference gpsRef;
	private IAirbag airbag;
	private ServiceReference airbagRef;
	private EmergencyMonitor moniter;
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Launching...");
		gpsRef = context.getServiceReference(IGps.class.getName());
		airbagRef = context.getServiceReference(IAirbag.class.getName());
		moniter = new EmergencyMonitor();
		if(gpsRef == null|| airbagRef == null){
			System.err.println("Unable to acquire service of GPS or Airbag");
			return;
		}
		gps = (IGps) context.getService(gpsRef);
		airbag = (IAirbag) context.getService(airbagRef);
		if(gps == null|| airbag == null){
			System.err.println("Unable to acquire GPS or Airbag");
			return;
		}
		moniter.setGps(gps);
		moniter.setAirbag(airbag);
		moniter.startup();
		//airbag.deploy();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		moniter.shutdown();
		if(gpsRef != null){
			context.ungetService(gpsRef);
		}
		if(airbagRef != null){
			context.ungetService(airbagRef);
		}
		System.out.println("Terminating");

	}

}
