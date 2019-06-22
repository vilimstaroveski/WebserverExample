package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * Implementation of {@link IFunction} that divides two numbers.
 * 
 * @author Vilim Staroveški
 *
 */
public class DivFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		Object value1 = tempStack.pop();
		Object value2 = tempStack.pop();
		
		double num1 = value1 instanceof String ? 
				Double.parseDouble((String) value1) : 
					 Double.valueOf(value1.toString());
		double num2 = value2 instanceof String ? 
				Double.parseDouble((String) value2) : 
					Double.valueOf(value2.toString());
		
		tempStack.push(num1 / num2);
	}

}
