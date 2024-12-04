package br.ufrn.imd.patterns.basicremoting;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import br.ufrn.imd.message.HttpMessage;

public class Marshaller {
	
	public String marshall(HttpMessage message) throws IOException {
		JSONObject json = new JSONObject();
		json.put("method", message.method());
		json.put("resource", message.resource());
		json.put("body", message.body());
		
		return json.toString();
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
	    
	    int contentLength = 0;
	    String[] headers = rawRequest.toString().split("\n");
	    for (String header : headers) {
	        if (header.startsWith("Content-Length:")) {
	            contentLength = Integer.parseInt(header.split(":")[1].trim());
	            break;
	        }
	    }
	    
	    char[] bodyChars = new char[contentLength];
	    reader.read(bodyChars, 0, contentLength);
	    String jsonMessage = new String(bodyChars);
	    System.out.println("Received JSON message: " + jsonMessage);
	    JSONObject json = new JSONObject(jsonMessage);
	    return new HttpMessage(
	        method,
	        resource,
	        json.getJSONObject("body")
	    );
	    
	    /*String jsonMessage = null;
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
	    return new HttpMessage(method, resource, new JSONObject());*/
	}
}
