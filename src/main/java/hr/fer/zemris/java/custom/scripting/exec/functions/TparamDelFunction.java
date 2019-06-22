package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that removes parameter, defined by name
 * as peek element on {@link Stack}, from temporary parameters in {@link RequestContext}
 * 
 * @author Vilim Staroveški
 *
 */
public class TparamDelFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		String name =  tempStack.pop().toString();
		
		requestContext.removeTemporaryParameter(name);
	}

}
