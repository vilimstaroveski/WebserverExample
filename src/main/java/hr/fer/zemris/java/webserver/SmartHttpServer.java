package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;
/**
 * Server that can execute smart scripts and also other expected functions
 * as a simple server. 
 * Program asks user when wants server to start running. User can type "START"
 * for running server, "STOP" for stopping it and "EXIT" to exit program.
 * 
 * @author Vilim Staroveški
 *
 */
public class SmartHttpServer {

	/**
	 * Server address.
	 */
	private String address;
	/**
	 * Port from where server is runned.
	 */
	private int port;
	/**
	 * Threads that represents workers that serves clients.
	 */
	private int workerThreads;
	/**
	 * Time after session time outs
	 */
	private int sessionTimeout;
	/**
	 * Mime types supported by this server.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Thread on which server is runned.
	 */
	private ServerThread serverThread;
	/**
	 * Pool of thread for new clients.
	 */
	private ExecutorService threadPool;
	/**
	 * File in which server root is.
	 */
	private Path documentRoot;
	/**
	 * Runnable representing a job of cleaning expired sessions and their cookies.
	 */
	private Runnable cleaningJob;
	/**
	 * Class representing a session of some client.
	 * @author Vilim Staroveški
	 *
	 */
	private static class SessionMapEntry {
		
		/**
		 * Session id given as 20 uppercase letters string.
		 */
		String sid;
		/**
		 * Time until this session expires.
		 */
		long validUntil;
		/**
		 * Map containing all cookies of this session.
		 */
		Map<String,String> map;
		/**
		 * Creates new {@link SessionMapEntry}.
		 * @param sid session id given as 20 uppercase letters string.
		 * @param validUntil Time until this session expires.
		 */
		public SessionMapEntry(String sid, long validUntil) {
			
			this.sid = sid;
			this.validUntil = validUntil*1000 + System.currentTimeMillis();
			map = new HashMap<String, String>();
		}
		/**
		 * Renewing session expire time.
		 * @param sessionTimeout server session time out time.
		 */
		public void updateTime(long sessionTimeout) {
			
			validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
		}
	}
	/**
	 * Map containing all sessions that was held on this server. When some
	 * session expires, utility thread removes it from map.
	 */
	private Map<String, SessionMapEntry> sessions =
			new HashMap<String, SmartHttpServer.SessionMapEntry>();
	/**
	 * Random for generating a session id.
	 */
	private Random sessionRandom = new Random();

	/**
	 * Creates new {@link SmartHttpServer} and configurates it from configuration
	 * file with name given as parameter configFileName
	 * @param configFileName path to configuration file.
	 */
	public SmartHttpServer(String configFileName) {
		
		configurateServer(configFileName);

		serverThread = new ServerThread();
		
		createCleaningThread();
	}
	/**
	 * Creates a utility thread that cleans up the session map 
	 * from expired sessions
	 */
	private void createCleaningThread() {

		cleaningJob = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					SwingUtilities.invokeLater(() -> {
						
						Set<String> keys = SmartHttpServer.this.sessions.keySet();
						for(String key : keys) {
							
							SessionMapEntry entry = SmartHttpServer.this.sessions.get(key);
							if(entry.validUntil < System.currentTimeMillis()) {
								SmartHttpServer.this.sessions.remove(key);
							}
						}
					});
					try {
						Thread.sleep(300000);
					}catch(Exception e) {
					}
				}
			}
		};
		Thread t = new Thread(cleaningJob);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Returns generated session id as 20 uppercase letters
	 * @return generated session id as 20 uppercase letters
	 */
	private String generateSID() {
		
		String sid = "";
		for(int i = 0; i < 20; i++) {
			//taking a random number from 64 to 90
			int rand = 65 + Math.abs((SmartHttpServer.this.sessionRandom.nextInt()) % 26);
			sid += Character.toString((char) rand);
		}
		return sid;
	}
	/**
	 * Configurates server from .properties file given by configFileName
	 * @param configFileName path to .properties file
	 */
	private void configurateServer(String configFileName) {

		Properties serverProp = new Properties();
		try {
			serverProp.load(Files.newInputStream(Paths.get(configFileName),
					StandardOpenOption.READ));
		} catch (IOException ignorable) {
		}
		
		address = (String) serverProp.get("server.address");
		port = Integer.parseInt(serverProp.get("server.port").toString());

		workerThreads = Integer.parseInt(serverProp.get("server.workerThreads")
				.toString());

		sessionTimeout = Integer.parseInt(serverProp.get("session.timeout")
				.toString());

		documentRoot = Paths
				.get((String) serverProp.get("server.documentRoot"));

		
		configurateMimes(serverProp);
	}

	/**
	 * Configurates mime types map of this server depending on .properties file 
	 * serverProp 
	 * @param serverProp path to .properties file.
	 */
	private void configurateMimes(Properties serverProp) {
		
		Properties mimeProp = new Properties();
		try {
			mimeProp.load(Files.newInputStream(
					Paths.get(serverProp.getProperty("server.mimeConfig")),
					StandardOpenOption.READ));
		} catch (IOException ignorable) {
		}

		Set<Object> keySet = mimeProp.keySet();
		for (Object key : keySet) {
			mimeTypes.put((String) key, (String) mimeProp.get(key));
		}
	}

	/**
	 * Starts server thread.
	 */
	protected synchronized void start() {
		
		serverThread.start();
		threadPool = Executors.newFixedThreadPool(workerThreads);
	}

	/**
	 * Stops server thread.
	 */
	@SuppressWarnings("deprecation")
	protected synchronized void stop() {
		
		if (serverThread.isAlive()) {
			serverThread.stop();
			serverThread.close();
		}
		threadPool.shutdown();
	}

	/**
	 * Thread that runs the server.
	 * @author Vilim Staroveški
	 *
	 */
	protected class ServerThread extends Thread {
		
		/**
		 * Socket on which server is listening.
		 */
		private ServerSocket serverSocket;
		
		public void close() {
			if(serverSocket != null) {
				try {
					
					serverSocket.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void run() {

			try {

				this.serverSocket = new ServerSocket();

			} catch (IOException e) {
				System.err.println("IO error occured when setting server socket!");
				System.err.println(e.getMessage());
			}
			try {
				serverSocket.bind(new InetSocketAddress((InetAddress) null,
						port));

			} catch (IOException e) {
				System.err.println("IO error occured when binding server socket!");
				System.err.println(e.getMessage());
			}
			while (true) {
				Socket client = null;
				try {

					client = serverSocket.accept();

				} catch (IOException e) {
					System.err.println("IO error occured when waiting for a connection");
					System.err.println(e.getMessage());
				}
				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}
		}
	}

	/**
	 * Class that represents a client that has entered this server.
	 * @author Vilim Staroveški
	 *
	 */
	private class ClientWorker implements Runnable {

		/**
		 * Socket on which client is connected.
		 */
		private Socket csocket;
		/**
		 * Input stream from client to this server where client sends
		 * its requests.
		 */
		private PushbackInputStream istream;
		/**
		 * Output stream where client gets requested content.
		 */
		private OutputStream ostream;
		/**
		 * Version of HTTP protocol.
		 */
		private String version;
		/**
		 * Method that client has called.
		 */
		private String method;
		/**
		 * Map containing parameters of client request.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		/**
		 * Map containing persistant parameters of client request.
		 */
		private Map<String, String> permPrams = null;
		/**
		 * List of output cookies.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/**
		 * Session ID given as 20 uppercase letters.
		 */
		private String SID;
		/**
		 * Creates new {@link ClientWorker}.
		 * @param csocket socket on which client is connected.
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				
				this.istream = new PushbackInputStream(
						csocket.getInputStream()
						);
				this.ostream = new BufferedOutputStream(
						csocket.getOutputStream()
						);

				List<String> lines = readRequest();
				if (lines == null) {
					sendError(ostream, 400, "Bad request");
					return;
				}
				String[] firstLine = lines.isEmpty() ? null : lines.get(0)
						.split(" ");
				
				checkFirstLine(firstLine);
				checkSession(lines);
				
				String path = getPath(firstLine[1]);
				Path pathToFile = Paths.get(documentRoot.toString(), path);
				checkForParameters(firstLine[1]);
				
				IWebWorker worker = getWorker(path);
				if( worker != null ) {
					worker.processRequest(new RequestContext(
							ostream, params, permPrams, outputCookies
							));
					return;
				}
				checkRequestedFile(pathToFile);
				
				String extension = getExtension(path);
				
				String mime = mimeTypes.get(extension) == null ? 
						"application/octet-stream" :
						mimeTypes.get(extension);

				RequestContext rc = new RequestContext(ostream, params,
						permPrams, outputCookies);
				rc.setMimeType(mime);
				rc.setStatusCode(200);
				
				if(extension.equalsIgnoreCase("smscr")) {
					runScript(pathToFile, rc);
					return;
				}
				try {
					presentFileToClient(pathToFile, rc);
				}catch (IOException ignorable) {
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		/**
		 * Method that checks if session is expired and if not 
		 * sets persistant parameters to values of old session.
		 * @param lines lines of client request.
		 */
		private synchronized void checkSession(List<String> lines) {
			
			String sidCandidate = "";
			for(String header : lines) {
				if(!header.startsWith("Cookie")) {
					continue;
				}
				String allCookies = header.split(" ")[1];
				String[] cookies = allCookies.split(";");
				for(String cookie : cookies) {
					String cookieName = cookie.split("=")[0];
					if(cookieName.equals("sid")) {
						sidCandidate = cookie.substring(cookie.indexOf("="));
						sidCandidate = sidCandidate.substring(2, sidCandidate.length()-1);
						break;
					}
				}
			}
			SessionMapEntry entry = null;
			
			if(sidCandidate.isEmpty()) {
				
				entry = newClientGenerated();
			}
			else {
				
				entry = sessions.get(sidCandidate);
				if(entry == null) {
					entry = newClientGenerated();
				}
				else if( entry.validUntil < System.currentTimeMillis() ) {
					
					entry = newClientGenerated();
				}
				else {
					entry.updateTime(sessionTimeout);
				}
			}
			
			SID = entry.sid;
			permPrams = entry.map;
		}

		/**
		 * Generates new session for current client
		 * @return new session for current client
		 */
		public synchronized SessionMapEntry newClientGenerated() {
			
			SessionMapEntry entry = new SessionMapEntry(generateSID(), SmartHttpServer.this.sessionTimeout);
			sessions.put(entry.sid, entry);
			outputCookies.add(new RCCookie( "sid", entry.sid, null, SmartHttpServer.this.address, "/", "HTTPOnly"));
			return entry;
		}

		/**
		 * Returns a web worker defined by path parameter.
		 * @param path path to worker.
		 * @return a web worker defined by path parameter. If does not exists, returns null.
		 */
		private IWebWorker getWorker(String path) {

			if(!path.startsWith("/ext/")) {
				return null;
			}
			String className = "hr.fer.zemris.java.webserver.workers." + 
								path.substring("/ext/".length()
										);
			Class<?> referenceToClass = null;
			try {
				referenceToClass = this.getClass().getClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				System.err.println("Class "+referenceToClass+" not found!");
				System.err.println(e.getMessage());
				return null;
			}
			Object newObject = null;
			try {
				newObject = referenceToClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			IWebWorker iww = (IWebWorker)newObject;
			return iww;
		}
		/**
		 * Runs script defined by path.
		 * @param path path to script
		 * @param rc request of client.
		 * @throws IOException if an IO error ocurres while running the script.
		 */
		private void runScript(Path path, RequestContext rc) throws IOException {

			String documentBody = new String(Files.readAllBytes(path),
					StandardCharsets.UTF_8);
			rc.setMimeType("text/plain");
			new SmartScriptEngine(
					new SmartScriptParser(documentBody).getDocumentNode(),
					rc).execute();
			rc.close();
		}
		/**
		 * Presents a file to client defined with path to file.
		 * @param path path to wanted file
		 * @param rc client request.
		 * @throws IOException if an IO error ocurres while reading a file.
		 */
		private void presentFileToClient(Path path, RequestContext rc) throws IOException {
			
			BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ));
			byte[] data = new byte[1024];
			while(true) {
				int r = bis.read(data);
				if(r < 0) {
					break;
				}
				rc.write(data);
			}
			rc.close();
		}
		/**
		 * Returns file extension on location path.
		 * @param path path where file is.
		 * @return file extension on location path.
		 */
		private String getExtension(String path) {

			String[] parts = path.split("\\.");
			return parts[parts.length - 1];
		}
		/**
		 * Checks if requested file is valid. If file is actually folder or
		 * does not exists or is not readable, error message will be send to client.
		 * @param path path to requested file.
		 * @throws IOException if error ocures in sending error to client.
		 */
		private void checkRequestedFile(Path path) throws IOException {

			if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) || 
					!Files.exists(path, LinkOption.NOFOLLOW_LINKS) ||
					!Files.isReadable(path)) {
				sendError(ostream, 403, "Path Forbidden");
				return;
			}
		}
		/**
		 * Checks if client request has any parameters.
		 * @param string with client request.
		 */
		private void checkForParameters(String string) {
			
			if(string.contains("?")) {
				parseParams(string.split("\\?")[1]);
			}
		}
		/**
		 * Extracts path from client request.
		 * @param string client request.
		 * @return path from client request.
		 */
		private String getPath(String string) {
			
			if(string.contains("?")) {
				return string.split("\\?")[0];
			}
			return string;
		}
		/**
		 * Checks if first line of client request is valid. If not sends error to client.
		 * @param firstLine first line of request.
		 * @throws IOException if IO error ocures while sending a error to client
		 */
		private void checkFirstLine(String[] firstLine) throws IOException {

			if (firstLine == null || firstLine.length != 3) {
				sendError(ostream, 400, "Bad request");
				return;
			}

			method = firstLine[0].toUpperCase();
			if (!method.equals("GET")) {
				sendError(ostream, 405, "Method Not Allowed");
				return;
			}
			version = firstLine[2].toUpperCase();
			if (!version.equals("HTTP/1.1")) {
				sendError(ostream, 505, "HTTP Version Not Supported");
				return;
			}
		}
		/**
		 * Parses parameters from client request and puts them in parmsMap of client.
		 * @param paramString client request.
		 */
		private void parseParams(String paramString) {

			String[] couples = paramString.split("&");
			for (String couple : couples) {
				if (!couple.contains("=")) {
					continue;
				}
				String[] oneCouple = couple.split("=");
				params.put(oneCouple[0], oneCouple[1]);
			}
		}
		/**
		 * Reads request from client.
		 * @return list of headers of clients request.
		 * @throws IOException if IO error ocures while reading request.
		 */
		private List<String> readRequest() throws IOException {

			String requestAsString = "";
			List<String> lines = new ArrayList<String>();
			int last, secondLast='a', thirdLast='b';
			while ((last = istream.read()) != -1) {
				
				requestAsString += Character.toString((char) last);
				if( (last == '\n' && secondLast == '\n') ||
					(last == '\n' && secondLast == '\r' && thirdLast == '\n') ) {
					//end of text
					lines.add(requestAsString.trim());
					return lines;
				}
				if( (last == '\n' && secondLast == '\r') ||
					(last == '\n') ) {
					//end of line
					lines.add(requestAsString.trim());
					requestAsString = "";
				}
				thirdLast = secondLast;
				secondLast = last;
			}
			return lines;
		}
		/**
		 * Method that sends error to client as defined in HTTP protocol.
		 * @param os output stream where client is.
		 * @param statusCode status code of reported error.
		 * @param statusText status text of reported error.
		 * @throws IOException if IO error occures while sending error
		 */
		private void sendError(OutputStream os, 
				int statusCode, String statusText) throws IOException {

			String response = "<html><head><title>"+statusText+"</title></head>"
					+ "<body><b>"+statusCode+" "+statusText+"</b></body><html>";
			os.write(
					("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
					"Server: simple java server\r\n"+
					"Content-Type: text/html;charset=UTF-8\r\n"+
					"Content-Length: "+response.length()+"\r\n"+
					"Connection: close\r\n"+
					"\r\n"+response).getBytes(StandardCharsets.US_ASCII));
			os.flush();
		}
	}
	/**
	 * Method called on program start. Prints out a greating message.
	 * @param args command line arguments, but not used in this method.
	 */
	public static void main(String[] args) {
		
		System.out.println(
				"Welcome to SmartHTTPServer!\n" +
				"If you want server to start, type \"START\".\n" +
				"You can turn off server by typing \"STOP\"\n" +
				"Once the server is stopped, it can only be restarted by typing \"Start\"\n" +
				"To close the program, type \"EXIT\"\n"
		);
		checkForUserInput();
	}
	/**
	 * Method that asks user what to do with server. Server can be started, stopped and program 
	 * can be shut down.
	 */
	private static void checkForUserInput() {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boolean serverRunning = false;
		SmartHttpServer server = null;
		while(true) {
			
			try {
				System.out.print(">");
				String line = reader.readLine();
				if(line.equalsIgnoreCase("START")) {
					if(serverRunning) {
						System.out.println("Server is already running!");
						continue;
					}
					System.out.println("Starting server...");
					serverRunning = true;
					server = new SmartHttpServer("config/server.properties");
					server.start();
				}
				else if(line.equalsIgnoreCase("STOP")) {
					if(!serverRunning) {
						System.out.println("Server is not running!");
						continue;
					}
					System.out.println("Shutting down server...");
					serverRunning = false;
					server.stop();
				}
				else if(line.equalsIgnoreCase("EXIT")) {
					if(serverRunning) {
						System.out.println("Shutting down server...");
						server.stop();
					}
					System.out.println("Thank you for using this server!");
					System.exit(1);
				}
				else {
					System.out.println("Unknown command");
				}
			} catch (IOException e) {
				System.err.println("An IO error occured!");
			}
		}
	}
}
