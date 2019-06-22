package hr.fer.zemris.java.custom.scripting.nodes;


/**
 * This class is node representing a piece of textual data. It inherits from Node class.
 * @author Vilim Staroveški
 *
 */
public class TextNode extends Node {
	/**
	 * Private property {@code String} text
	 */
	private String text;
	/**
	 * Constructor that sets private property text to given {@code String} value
	 * @param text text in node
	 */
	public TextNode(String text) {
		this.text = text;
	}
	/**
	 * Returns property text of this class
	 * @return text in node
	 */
	public String getText() {
		return text;
	}
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
