package br.ufrn.imd.patterns.basicremoting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;

import br.ufrn.imd.message.HttpMessage;

public class Marshaller {
	
	public void marshall(BufferedWriter writer, HttpMessage message) throws IOException {
		JSONObject json = new JSONObject();
		json.put("method", message.method());
		json.put("resource", message.resource());
		json.put("body", message.body());
		
		writer.write(json.toString());
		writer.newLine();
		writer.flush();
	}
	
	public HttpMessage unmarshall(BufferedReader reader) throws IOException {
		String jsonMessage = reader.readLine();
		JSONObject json = new JSONObject(jsonMessage);
		
		return new HttpMessage(
				json.getString("method"),
				json.getString("resource"),
				json.getJSONObject("body")
		);
	}
}
