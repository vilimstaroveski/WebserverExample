package hr.fer.zemris.java.custom.scripting.tokens;
/**
 * This class inherits Token and has single read-only String property: symbol
 * @author Vilim Staroveški
 *
 */
public class TokenOperator extends Token {
	/**
	 * Private {@code String} symbol value
	 */
	private String symbol;
	/**
	 * Constructor that sets this class private symbol to given {@code String} value
	 * @param symbol symbol for token
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}
	/**
	 * Returns private {@code String} symbol of this class
	 * @return {@code String} symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * Returns private {@code String} symbol of this class
	 * @return {@code String} symbol
	 */
	@Override
	public String asText() {
		return symbol;
	}
}
