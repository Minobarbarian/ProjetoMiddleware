package br.ufrn.imd.application;

import java.io.IOException;

import br.ufrn.imd.broker.Nelionator;
import br.ufrn.imd.business.SmartHome;

public class Main {

	public static void main(String[] args) throws IOException {
		SmartHome smart = new SmartHome("OFF", 22);
		
		Nelionator server = new Nelionator();
		
		server.addComponents(smart);
		
		server.start(4242);
	}

}
