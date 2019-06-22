package hr.fer.zemris.java.webserver;
/**
 * Interface for web worker that does some job on web server.
 * 
 * @author Vilim Staroveški
 *
 */
public interface IWebWorker {

	/**
	 * Processes request from client given as {@link RequestContext} element
	 * @param context request from client.
	 */
	public void processRequest(RequestContext context);
}
