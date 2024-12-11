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
	    
	    //lê tudo até não ter mais linhas e armazena no rawRequest
	    while ((line = reader.readLine()) != null && !line.isEmpty()) {
	        rawRequest.append(line).append("\n");
	    }
	    
	    //separa o conteudo do rawRequest em coisas uteis
	    String[] requestLine = rawRequest.toString().split("\n")[0].split(" ");
	    String method = requestLine[0];
	    String resource = requestLine[1];
	    
	    //pega os cabeçalhos do rawRequest
	    int contentLength = 0;
	    String[] headers = rawRequest.toString().split("\n");
	    for (String header : headers) {
	        if (header.startsWith("Content-Length:")) {
	            contentLength = Integer.parseInt(header.split(":")[1].trim());
	            break;
	        }
	    }
	    
	    //pega o corpo (se tiver) do rawRequest
	    JSONObject jsonBody = new JSONObject();
	    if (contentLength > 0) {
	    	String[] parts = rawRequest.toString().split("\n\n", 2);
	    	String body = parts.length > 1 ? parts[1] : "";
	    	jsonBody = body.isEmpty() ? new JSONObject() : new JSONObject(body);
	    }

	    //compoe a mensagem http
	    return new HttpMessage(
	        method,
	        resource,
	        jsonBody.optJSONObject("body") != null ? jsonBody.getJSONObject("body") : new JSONObject()
	    );
	}
}
