package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that swaps last two elements 
 * on stack.
 * 
 * @author Vilim Staroveški
 *
 */
public class SwapFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		Object a = tempStack.pop();
		Object b = tempStack.pop();
		
		tempStack.push(a);
		tempStack.push(b);
	}

}
