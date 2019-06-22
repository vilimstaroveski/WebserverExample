package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
/**
 * This class is node representing a single for-loop construct. It inherits from Node class.
 * @author Vilim Staroveški
 *
 */
public class ForLoopNode extends Node {
	/**
	 * Private property {@code TokenVariable} variable
	 */
	private TokenVariable variable;
	/**
	 * Private property {@code Token} start expression
	 */
	private Token startExpression;
	/**
	 * Private property {@code Token} end expression
	 */
	private Token endExpression;
	/**
	 * Private property {@code Token} step expression, can be null!
	 */
	private Token stepExpression;
	/**
	 * Constructor that sets all private properties to given values.
	 * @param variable variable
	 * @param startExpression from where to start counting
	 * @param endExpression to which value to count
	 * @param stepExpression step of counting
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression, Token endExpression, Token stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	/**
	 * Returns private {@code TokenVariable} variable of this class 
	 * @return {@code TokenVariable}
	 */
	public TokenVariable getVariable() {
		return variable;
	}
	/**
	 * Returns private {@code Token} startExpression of this class 
	 * @return {@code Token}
	 */
	public Token getStartExpression() {
		return startExpression;
	}
	/**
	 * Returns private {@code Token} endExpression of this class 
	 * @return{@code Token}
	 */
	public Token getEndExpression() {
		return endExpression;
	}
	/**
	 * Returns private {@code Token} stepExpression of this class 
	 * @return {@code Token}
	 */
	public Token getStepExpression() {
		return stepExpression;
	}
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
} 
