package hr.fer.zemris.java.custom.scripting.nodes;


/**
 * This class is node representing an entire document. It inherits from {@code Node} class.
 * @author Vilim Staroveški
 *
 */
public class DocumentNode extends Node {
	/**
	 * Default constructor
	 */
	public DocumentNode() {
		super();
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
