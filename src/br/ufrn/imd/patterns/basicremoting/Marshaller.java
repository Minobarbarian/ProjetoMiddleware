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
        
        // Read headers until the empty line
        while ((line = reader.readLine()) != null) {
            rawRequest.append(line).append("\n");
            
            // Check for the empty line separating headers from body
            if (line.trim().isEmpty()) {
                break;  // Stop reading once we hit the empty line
            }
        }
        
        // Debugging output for headers and request
        System.out.println("RAW REQUEST IS:");
        System.out.println(rawRequest);
        
        // Parse the request line (method and resource)
        String[] requestLine = rawRequest.toString().split("\n")[0].split(" ");
        String method = requestLine[0];
        String resource = requestLine[1];
        
        // Initialize Content-Length to 0
        int contentLength = 0;
        
        // Extract headers and find Content-Length
        String[] headers = rawRequest.toString().split("\n");
        for (String header : headers) {
            if (header.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(header.split(":")[1].trim());
                break;
            }
        }
        
        // Handle the body if there is one
        JSONObject jsonBody = new JSONObject();
        if (contentLength > 0) {
            // Read the body based on Content-Length
            char[] buffer = new char[contentLength];
            int bytesRead = reader.read(buffer, 0, contentLength);  // Read the exact body size
            if (bytesRead > 0) {
                String body = new String(buffer, 0, bytesRead).trim();
                System.out.println("Body Content Read: " + body);  // Debugging log for body content
                
                // Check if body is non-empty and parse it into JSON
                if (!body.isEmpty()) {
                    try {
                        jsonBody = new JSONObject(body);  // Parse the body into JSON
                        System.out.println("Parsed JSON Body: " + jsonBody);  // Debugging log for JSON body
                    } catch (Exception e) {
                        System.out.println("Error parsing JSON body: " + e.getMessage());
                    }
                }
            }
        }
        
        // Return the HttpMessage object with the parsed information
        return new HttpMessage(
            method,
            resource,
            jsonBody
        );
    }

}
