package hr.fer.zemris.java.custom.scripting.tokens;
/**
 * This class inherits Token and has single read-only String property: name
 * @author Vilim Staroveški
 *
 */
public class TokenFunction extends Token {
	/**
	 * Private property {@code String} name
	 */
	private String name;
	/**
	 * Constructor that sets private name of this class to given {@code String} value
	 * @param name name of function
	 */
	public TokenFunction(String name) {
		this.name = name;
	}
	/**
	 * Returns name of this class {@code String}
	 * @return name of function
	 */
	public String getName() {
		return name;
	}
	@Override
	public String asText() {
		return name;
	}
}
