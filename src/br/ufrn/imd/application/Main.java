package br.ufrn.imd.application;

import java.io.IOException;

import br.ufrn.imd.businness.SmartHome;
import br.ufrn.imd.middleware.Nelionator;

public class Main {

	public static void main(String[] args) throws IOException {
		SmartHome smart = new SmartHome();
		
		Nelionator server = new Nelionator(4242);
		
		server.addComponents(smart);
		
		server.start();
	}
}