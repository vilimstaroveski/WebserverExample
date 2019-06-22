package hr.fer.zemris.java.custom.scripting.exec;

public class NonExistingStackException extends RuntimeException{

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new {@link NonExistingStackException} initialised with messege
	 * @param message {@link String} representing a initial messege explaining the exception.
	 */
	public NonExistingStackException(String message) {
		super(message);
	}
}
