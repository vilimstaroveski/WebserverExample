package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Interface for functions that uses {@link Stack} and {@link RequestContext} 
 * for preforming operations called by method apply.
 * @author Vili
 *
 */
public interface IFunction {

	/**
	 * Operation with {@link Stack} and {@link RequestContext}.
	 * @param tempStack {@link Stack} for taking values and storing results.
	 * @param requestContext {@link RequestContext} for storing changes.
	 */
	public void apply(Stack<Object> tempStack, RequestContext requestContext);
}
