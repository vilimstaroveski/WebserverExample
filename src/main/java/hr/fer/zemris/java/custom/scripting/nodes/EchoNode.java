package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
/**
 * This class is node representing a command which generates some textual output dynamically. It inherits
 * from Node class.
 * @author Vilim Staroveški
 *
 */
public class EchoNode extends Node {
	/**
	 * Private property array of tokens
	 */
	private Token[] tokens;
	/**
	 * Constructor that sets private tokens.
	 * @param tokens tokens from this node
	 */
	public EchoNode(Token[] tokens) {
		
		super();
		this.tokens = tokens;
	}
	/**
	 * Returns private tokens of this class
	 * @return array of tokens
	 */
	public Token[] getTokens() {
		
		return tokens;
	}
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
	
}
