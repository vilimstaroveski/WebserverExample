package hr.fer.zemris.java.custom.scripting.tokens;
/**
 * This class inherits Token and has single read-only double property: value
 * @author Vilim Staroveški
 *
 */
public class TokenConstantDouble extends Token {
	/**
	 * Private property {@code double} value
	 */
	private double value;
	/**
	 * Constructor that sets private value 
	 * @param value value of token
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
	}
	/**
	 * Returns private property value of this class
	 * @return value value of token
	 */
	public double getValue() {
		return value;
	}
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
