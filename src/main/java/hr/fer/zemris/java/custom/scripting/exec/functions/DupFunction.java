package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that duplicates value on stack.
 * 
 * @author Vilim Staroveški
 *
 */
public class DupFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		Object value = tempStack.pop();
		tempStack.push(value);
		tempStack.push(value);
	}

}
