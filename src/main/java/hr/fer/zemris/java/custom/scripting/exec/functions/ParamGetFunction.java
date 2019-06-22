package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that gets parameter from {@link RequestContext} 
 * defined by name from last element on {@link Stack} and pushes fetched element on {@link Stack}.
 * 
 * @author Vilim Staroveški
 *
 */
public class ParamGetFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		String defValue = tempStack.pop().toString();
		String name =  tempStack.pop().toString();
		
		String value = requestContext.getParameter(name);
		
		tempStack.push(value == null ? defValue : value);
	}

}
