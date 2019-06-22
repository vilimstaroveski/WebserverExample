package hr.fer.zemris.java.custom.scripting.tokens;
/**
 * This class inherits Token and has single read-only String property: value
 * @author Vilim Staroveški
 *
 */
public class TokenString extends Token {
	/**
	 * Private {@code String} property value.
	 */
	private String value;
	/**
	 * Constructor that sets private {@code String} to given value
	 * @param value - wanted value
	 */
	public TokenString(String value) {
		
		this.value = value.replace("\\n", "\n");
		this.value = this.value.replace("\\r", "\r");
		this.value = this.value.replace("\\t", "\t");
	}
	/**
	 * Returns private {@code String} value of this class
	 * @return {@code String} value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Returns private {@code String} value of this class
	 * @return value
	 */
	@Override
	public String asText() {
		return "\""+value+"\"";
	}
}
