package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstration for {@link SmartScriptEngine}.
 * @author Vilim Staroveški
 *
 */
public class DemoSmartScriptEngine {

	/**
	 * Method called at program start. User should uncomment wanted demo for demonstration.
	 * @param args command line arguments, not used in this program.
	 */
	public static void main(String[] args) {

//		 demo1();
//			demo2();
//		 demo3();
//		demo4();

	}

	/**
	 * Method for testing work of {@link SmartScriptEngine} on script "fibonacci.smscr"
	 */
	private static void demo4() {
		
		String documentBody = null;

		try {
			documentBody = new String(Files.readAllBytes(Paths
					.get("webroot/scripts/fibonacci.smscr")),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Invalid path or problem reading file!");
			System.exit(-1);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();

	}

	/**
	 * Method for testing work of {@link SmartScriptEngine} on script "brojPoziva.smscr"
	 */
	private static void demo3() {

		String documentBody = null;

		try {
			documentBody = new String(Files.readAllBytes(Paths
					.get("webroot/scripts/brojPoziva.smscr")),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Invalid path or problem reading file!");
			System.exit(-1);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters,
				persistentParameters, cookies);
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(), rc)
				.execute();
		System.out.println("\nVrijednost u mapi: "
				+ rc.getPersistentParameter("brojPoziva"));

	}

	/**
	 * Method for testing work of {@link SmartScriptEngine} on script "zbrajanje.smscr"
	 * @throws IOException 
	 */
	private static void demo2() {

		String documentBody = null;

		try {
			documentBody = new String(Files.readAllBytes(Paths
					.get("webroot/scripts/zbrajanje.smscr")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Invalid path or problem reading file!");
			System.exit(-1);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();
	}

	/**
	 * Method for testing work of {@link SmartScriptEngine} on script "osnovni.smscr"
	 */
	private static void demo1() {

		String documentBody = null;

		try {
			documentBody = new String(Files.readAllBytes(Paths
					.get("webroot/scripts/osnovni.smscr")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Invalid path or problem reading file!");
			System.exit(-1);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();

	}

}
