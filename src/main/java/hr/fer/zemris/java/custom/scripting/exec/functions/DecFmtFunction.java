package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.text.DecimalFormat;
import java.util.Stack;
/**
 * Implementation of {@link IFunction} that applys specified format on a number.
 * 
 * @author Vilim Staroveški
 *
 */
public class DecFmtFunction implements IFunction {

	@Override
	public void apply(Stack<Object> tempStack, RequestContext requestContext) {
		
		DecimalFormat format = new DecimalFormat(tempStack.pop().toString());
		Object value = tempStack.pop();
		double number = value instanceof String ? Double.parseDouble((String) value) : Double.valueOf(value.toString());;
		
		tempStack.push(format.format(number));
		
	}

}
