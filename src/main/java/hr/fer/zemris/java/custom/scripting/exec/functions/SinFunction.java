package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that pops last value
 * from stack and pushes sinus of that value as result on stack.
 * 
 * @author Vilim Staroveški
 *
 */
public class SinFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		Object value = tempStack.pop();
		double number = value instanceof String ? Double.parseDouble((String) value) : Double.valueOf(value.toString());;
		
		double numberInDegrees = Math.toRadians(number);
		
		tempStack.push(Math.sin(numberInDegrees));
	}

}
