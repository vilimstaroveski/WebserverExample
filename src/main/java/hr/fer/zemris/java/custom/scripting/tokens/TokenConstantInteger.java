package hr.fer.zemris.java.custom.scripting.tokens;
/**
 * This class inherits Token and has single read-only int property: value
 * @author Vilim Staroveški
 *
 */
public class TokenConstantInteger extends Token {
	/**
	 * Private property {@code int} value
	 */
	private int value;
	/**
	 * Constructor that sets private value 
	 * @param value value integer
	 */
	public TokenConstantInteger(int value) {
		this.value = value;
	}
	/**
	 * Returns private property value of this class
	 * @return valuein this token
	 */
	public int getValue() {
		return value;
	}
	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
