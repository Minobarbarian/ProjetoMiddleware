package br.ufrn.imd.patterns.basicremoting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufrn.imd.message.HttpMessage;


public class ServerRequestHandler {
	private ServerSocket servSocket;
	private final ExecutorService execService;
	private final Invoker invoker;
	private final Marshaller marshaller;
	
	public ServerRequestHandler(int port, Invoker invoker) {
		this.invoker = invoker;
		this.marshaller = new Marshaller();
		this.execService = Executors.newCachedThreadPool();
		start(port);
		System.out.println("Server started on port " + port);
		acceptConnections();
	}
	
	public void start(int port) {
		try {
			this.servSocket = new ServerSocket(port);
		} catch (Exception e) {
			String errorResponse = RemotingError.handleError(e);
			System.err.println(errorResponse);
		}
	}
	
	public void acceptConnections() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				Socket clientSocket = servSocket.accept();
				execService.execute(() -> handleRequest(clientSocket));
			} catch (IOException e) {
				String errorResponse = RemotingError.handleError(e);
				System.err.println(errorResponse);
			}
		}
	}
	
	private void handleRequest(Socket clientSocket) {
		try {
			HttpMessage request = readRequest(clientSocket);
			HttpMessage response = invoker.invoke(request);
			sendResponse(clientSocket, response);
		} catch (Exception e) {
			String errorResponse = RemotingError.handleError(e);
			HttpMessage errorHttpMessage = RemotingError.createErrorHttpMessage(errorResponse,"ERROR","response");
			try {
				sendResponse(clientSocket, errorHttpMessage);
			} catch (IOException ioException) {
				System.err.println("Failed to send error response: " + ioException.getMessage());
			}
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("Failed to close client socket: " + e.getMessage());
			}
		}
	}
	
	private HttpMessage readRequest(Socket clientSocket) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
			HttpMessage request = marshaller.unmarshall(reader);
			return request;
		}
	}
	
	private void sendResponse(Socket clientSocket, HttpMessage response) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
			marshaller.marshall(writer, response);
		}
	}
}