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
		StringBuilder rawRequest = new StringBuilder();
	    String line;
	    
	    while ((line = reader.readLine()) != null && !line.isEmpty()) {
	        rawRequest.append(line).append("\n");
	    }
	    
	    String[] requestLine = rawRequest.toString().split("\n")[0].split(" ");
	    String method = requestLine[0];
	    String resource = requestLine[1];
	    
	    String jsonMessage = null;
	    if (reader.ready()) {
	        jsonMessage = reader.readLine();
	    }
	    
	    if (jsonMessage != null) {
	        JSONObject json = new JSONObject(jsonMessage);
	        return new HttpMessage(
	            method,
	            resource,
	            json.getJSONObject("body")
	        );
	    }
	    return new HttpMessage(method, resource, new JSONObject());
	}
}
