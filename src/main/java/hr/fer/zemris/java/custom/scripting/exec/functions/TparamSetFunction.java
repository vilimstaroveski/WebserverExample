package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that sets parameter, defined by name
 * as peek element on {@link Stack}, in temporary parameters in {@link RequestContext}
 * with value as second last element on {@link Stack}.
 * 
 * @author Vilim Staroveški
 *
 */
public class TparamSetFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		String name =  tempStack.pop().toString();
		String value =  tempStack.pop().toString();
		
		requestContext.setTemporaryParameter(name, value);
	}

}
