package br.ufrn.imd.patterns.basicremoting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.ufrn.imd.message.HttpMessage;


public class ServerRequestHandler {
	private ServerSocket servSocket;
	private final ExecutorService execService;
	private final Invoker invoker;
	private final Marshaller marshaller;
	private volatile boolean isRunning = true;
	
	public ServerRequestHandler(int port, Invoker invoker) {
		this.invoker = invoker;
		this.marshaller = new Marshaller();
		this.execService = Executors.newVirtualThreadPerTaskExecutor();
		start(port);
		System.out.println("Server started on port " + port);
		acceptConnections();
	}
	
	public void start(int port) {
		int maxRetries = 3;
        int retryDelay = 2000;
        for (int i = 0; i < maxRetries; i++) {
            try {
                this.servSocket = new ServerSocket();
                this.servSocket.setReuseAddress(true);
                this.servSocket.bind(new InetSocketAddress(port));
                return;
            } catch (IOException e) {
                handleError(e);
                if (i == maxRetries - 1) {
                    System.err.println("Failed to bind to port after " + maxRetries + " retries. Exiting...");
                    System.exit(1);
                }
                System.out.println("Retrying to bind to port " + port + "...");
                try {
                    TimeUnit.MILLISECONDS.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
	}
	
	public void acceptConnections() {
		while(isRunning) {
			try {
				Socket clientSocket = servSocket.accept();
                System.out.println("Connection accepted: " + clientSocket.getRemoteSocketAddress());
				execService.execute(() -> handleRequest(clientSocket));
	        } catch (IOException IOe) {
	        	if(!isRunning) {
	        		break;
	        	}
	        	handleError(IOe);
			}
		}
	}
	
	private void handleRequest(Socket clientSocket) {
		try (clientSocket){
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			HttpMessage request = readRequest(clientSocket, reader);
            HttpMessage response = invoker.invoke(request);
            sendResponse(response, writer);
		} catch(Exception e) {
			handleSocketError(clientSocket, e);
		}
	}
	
	private HttpMessage readRequest(Socket clientSocket, BufferedReader reader) throws IOException {
		return marshaller.unmarshall(reader);
	}
	
	private void sendResponse(HttpMessage response, BufferedWriter writer) throws IOException {
		String responseBody = marshaller.marshall(response);
		writer.write("HTTP/1.1 200 OK\r\n");
		writer.write("Content-Type: application/json\r\n");
		byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
		writer.write("Content-Length: " + responseBytes.length + "\r\n");
	    writer.write("\r\n");
		writer.write(responseBody);
		writer.flush();
	}
	
	private void handleSocketError(Socket clientSocket, Exception e) {
	    System.err.println("Error handling request: " + e.getMessage());
	    String errorResponse = RemotingError.handleError(e);
	    HttpMessage errorHttpMessage = RemotingError.createErrorHttpMessage(errorResponse, "ERROR", "response");

	    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
	        sendResponseWithErrorHandling(errorHttpMessage, writer);
	    } catch (IOException ioException) {
	        System.err.println("Failed to send error response: " + ioException.getMessage());
	        ioException.printStackTrace();
	    }
	}

	
	private void sendResponseWithErrorHandling(HttpMessage errorHttpMessage, BufferedWriter writer) {
        try {
            sendResponse(errorHttpMessage, writer);
        } catch (IOException ioException) {
            System.err.println("Failed to send error response: " + ioException.getMessage());
        }
	}
	
	/*private void closeSocket(Socket clientSocket) {
		if (clientSocket != null && !clientSocket.isClosed()) {
			try {
				System.out.println("closed the socket");
	            clientSocket.close();
	        } catch (IOException e) {
	        	System.out.println("failed to close");
	            handleError(e);
	        }
		}
	}*/
	
	private void handleError(Exception e) {
		String errorResponse = RemotingError.handleError(e);
        System.err.println(errorResponse);
	}
	
	public void shutdown() {
        try {
            isRunning = false;
            servSocket.close();
            execService.shutdown();
            if (!execService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                execService.shutdownNow();
            }
            System.out.println("Server shutdown completed.");
        } catch (IOException | InterruptedException e) {
            handleError(e);
        }
    }
}