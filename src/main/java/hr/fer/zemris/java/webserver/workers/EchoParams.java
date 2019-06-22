package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;
/**
 * Implementation of {@link IWebWorker} interface. It prints out 
 * table of parameter name and value pairs given in client request.
 * 
 * @author Vilim Staroveški
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		
		context.setMimeType("text/html");
		try {
			context.write("<html>\r\n  <body>\r\n");
			context.write("    <h1>Tablica</h1>\r\n");
			context.write("      <table border='1'>\r\n");
			for(String name : context.getParameterNames()) {
				context.write("      <tr><td>");
				context.write(name);
				context.write("</td><td>");
				context.write(context.getParameter(name));
				context.write("</td></tr>\r\n");
			}
			context.write("      </table>\r\n  </body>\r\n</html>");
		} catch (IOException ex) {
			System.err.println("There was an IO error occured while writing to client.");
			System.err.println(ex.getMessage());
		}
		try {
			context.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
