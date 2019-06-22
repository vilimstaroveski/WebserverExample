package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that gets parameter, defined by name
 * as peek element on {@link Stack}, from temporary parameters in {@link RequestContext}
 * and stores it on {@link Stack}. If name does not exists, second last value from stack,
 * will be put on stack.
 * 
 * @author Vilim Staroveški
 *
 */
public class TparamGetFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		String defValue =  tempStack.pop().toString();
		String name =  tempStack.pop().toString();
		
		String value = requestContext.getTemporaryParameter(name);
		
		tempStack.push(value == null ? defValue : value);
	}

}
