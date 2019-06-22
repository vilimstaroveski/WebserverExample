package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Demonstration for {@link RequestContext} class.
 * @author Vilim Staroveški
 *
 */
public class DemoRequestContext {
	
	/**
	 * Method called at program start.
	 * @param args command line arguments, not used in this program.
	 * @throws IOException produced by failed or interrupted I/O operations.
	 */
	public static void main(String[] args) throws IOException {
		demo1("primjer1.txt", "ISO-8859-2");
		demo1("primjer2.txt", "UTF-8");
		demo2("primjer3.txt", "UTF-8");
	}

	/**
	 * Method that sets header and body of {@link RequestContext}.
	 * @param filePath {@link Path} to file in which result will be written.
	 * @param encoding encoding for writing result
	 * @throws IOException produced by failed or interrupted I/O operations.
	 */
	private static void demo1(String filePath, String encoding)
			throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
	}

	/**
	 * Method that sets header and body of {@link RequestContext}.
	 * @param filePath {@link Path} to file in which result will be written.
	 * @param encoding encoding for writing result
	 * @throws IOException produced by failed or interrupted I/O operations.
	 */
	private static void demo2(String filePath, String encoding)
			throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1",
				"/", null));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/", null));
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
	}
}