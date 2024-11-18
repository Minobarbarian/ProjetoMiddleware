package br.ufrn.imd.application;

import java.io.IOException;

import br.ufrn.imd.business.SmartHome;
import br.ufrn.imd.middleware.Nelionator;

public class Main {

	public static void main(String[] args) throws IOException {
		SmartHome smart = new SmartHome("OFF", 22);
		
		Nelionator server = new Nelionator();
		
		server.addComponents(smart);
		
		server.start(4242);
	}

}
