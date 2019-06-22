package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that sets mime type for {@link RequestContext}
 * 
 * @author Vilim Staroveški
 *
 */
public class SetMimeTypeFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		String text = tempStack.pop().toString();
		requestContext.setMimeType(text);
	}

}
