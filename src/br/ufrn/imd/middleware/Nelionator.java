package br.ufrn.imd.middleware;

import java.io.IOException;
import br.ufrn.imd.patterns.basicremoting.Invoker;
import br.ufrn.imd.patterns.basicremoting.ServerRequestHandler;
import br.ufrn.imd.patterns.identification.Lookup;

public class Nelionator {
	public ServerRequestHandler requestHandler;
	private final Invoker invoker;
	private final Lookup lookup;
	private final int port;
	
	public Nelionator(int port) {
		this.lookup = new Lookup();
		this.invoker = new Invoker(lookup);
		this.port = port;
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));
	}
	
	public void addComponents(Object object) {
		Class<?> component = object.getClass();
		lookup.registerRoute(component, port);
	}
	
	public void start() throws IOException {
		requestHandler = new ServerRequestHandler(port, invoker);
	}
	
	public void shutdown() {
		if(requestHandler != null) {
			requestHandler.shutdown();
			System.out.println("Server shutdown successfully via shutdown hook.");
		}
	}
}