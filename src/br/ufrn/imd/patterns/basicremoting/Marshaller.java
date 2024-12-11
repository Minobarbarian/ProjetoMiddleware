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
        
        // Le cabeçalhos ate a linha vazia
        while ((line = reader.readLine()) != null) {
            rawRequest.append(line).append("\n");
            
            // Ve se chegou na linha vazia que separa os cabeçalhos do corpo
            if (line.trim().isEmpty()) {
                break;
            }
        }
        
        // Guarda o metodo e o recurso
        String[] requestLine = rawRequest.toString().split("\n")[0].split(" ");
        String method = requestLine[0];
        String resource = requestLine[1];
        
        // Inicializa Content-Length
        int contentLength = 0;
        
        // Extrai cabeçalhos e descobre a largura do conteudo
        String[] headers = rawRequest.toString().split("\n");
        for (String header : headers) {
            if (header.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(header.split(":")[1].trim());
                break;
            }
        }
        
        // Lida com o corpo se tiver
        JSONObject jsonBody = new JSONObject();
        if (contentLength > 0) {
            char[] buffer = new char[contentLength];
            int bytesRead = reader.read(buffer, 0, contentLength);
            if (bytesRead > 0) {
                String body = new String(buffer, 0, bytesRead).trim();
                
                if (!body.isEmpty()) {
                    try {
                        jsonBody = new JSONObject(body);
                    } catch (Exception e) {
                    }
                }
            }
        }
        
        // Retorna HttpMessage com informaçao esclarecida
        return new HttpMessage(
            method,
            resource,
            jsonBody
        );
    }

}
