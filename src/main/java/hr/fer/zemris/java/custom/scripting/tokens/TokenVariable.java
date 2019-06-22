package hr.fer.zemris.java.custom.scripting.tokens;
/**
 * This method inherits Token, and has a single read-only {@code String} property: name.
 * @author Vilim Staroveški
 *
 */
public class TokenVariable extends Token {
	/**
	 * Read-only {@code String} property
	 */
	private String name;
	/**
	 * Constructor that sets name property of this class to given {@code String}
	 * @param name - Wanted name
	 */
	public TokenVariable(String name) {
		this.name = name;
	}
	/**
	 * Returns property name of this class
	 * @return name  of token 
	 */
	public String getName() {
		return name;
	}

	@Override
	public String asText() {
		return name;
	}
}
