package hr.fer.zemris.java.webserver;

import static org.junit.Assert.*;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class RequestContextTest {

	@Test
	public void testRequestContext() throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get("test.txt"));
		RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1",
				"/", null));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/", null));
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
		
		BufferedReader reader = Files.newBufferedReader(Paths.get("test.txt"));
		
		String[] expectedLines = new String[10];
		expectedLines[0] = "HTTP/1.1 205 Idemo dalje";
		expectedLines[1] = "Content-Type: text/plain; charset=UTF-8";
		expectedLines[2] = "Set-Cookie: korisnik=\"perica\"; Domain=127.0.0.1; Path=/; Max-Age=3600";
		expectedLines[3] = "Set-Cookie: zgrada=\"B4\"; Path=/";
		expectedLines[4] = "";
		expectedLines[5] = "Čevapčići i Šiščevapčići.";
		for(String line : expectedLines) {
			assertEquals(line, reader.readLine());
		}
		
		for(String name : rc.getParameterNames()) {
			assertNotNull(rc.getParameter(name));
		}
		rc.setPersistentParameter("persistant", "test value");
		for(String name : rc.getPersistentParameterNames()) {
			assertNotNull(rc.getPersistentParameter(name));
		}
		rc.setTemporaryParameter("temp", "value");
		for(String name : rc.getTemporaryParameterNames()) {
			assertNotNull(rc.getTemporaryParameter(name));
		}
		rc.removePersistentParameter("persistant");
		rc.removeTemporaryParameter("temp");
	}
	
	@Test
	public void testRequestContextReadingBytes() throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get("test.txt"));
		RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.".getBytes());
		os.close();
		
		BufferedReader reader = Files.newBufferedReader(Paths.get("test.txt"));
		
		String[] expectedLines = new String[4];
		expectedLines[0] = "HTTP/1.1 205 Idemo dalje";
		expectedLines[1] = "Content-Type: text/plain; charset=UTF-8";
		expectedLines[2] = "";
		expectedLines[3] = "Čevapčići i Šiščevapčići.";
		for(String line : expectedLines) {
			assertEquals(line, reader.readLine());
		}
		
		for(String name : rc.getParameterNames()) {
			assertNotNull(rc.getParameter(name));
		}
		rc.setPersistentParameter("persistant", "test value");
		for(String name : rc.getPersistentParameterNames()) {
			assertNotNull(rc.getPersistentParameter(name));
		}
		rc.setTemporaryParameter("temp", "value");
		for(String name : rc.getTemporaryParameterNames()) {
			assertNotNull(rc.getTemporaryParameter(name));
		}
		rc.removePersistentParameter("persistant");
		rc.removeTemporaryParameter("temp");
	}
	
	@Test
	public void testRequestContextExceptions() throws IOException {
		
		OutputStream os = Files.newOutputStream(Paths.get("test.txt"));
		RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1",
				"/", null));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/", null));
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
		
		RCCookie cookie = new RCCookie("name", "perica2", 200, "localhost", "/", "HTTPOnly");
		assertEquals("name", cookie.getName());
		assertEquals("perica2", cookie.getValue());
		assertEquals("200".toString(), cookie.getMaxAge().toString());
		assertEquals("localhost", cookie.getDomain());
		assertEquals("/", cookie.getPath());
		
		boolean exceptionThrown = false;
		try {
			rc.addRCCookie(new RCCookie("zgrada", "B3", null, null, "/", null));
		}catch(RuntimeException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		try {
			rc.setMimeType("text/html");
		}catch(RuntimeException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		try {
			rc.setStatusCode(205);
		}catch(RuntimeException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		try {
			rc.setStatusText("Ne idemo dalje");
		}catch(RuntimeException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		try {
			rc.setEncoding("UTF-16");
		}catch(RuntimeException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
	}
}
