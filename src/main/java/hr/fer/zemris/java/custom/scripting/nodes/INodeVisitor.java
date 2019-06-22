package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Interface for class that visits a {@link Node} in a 
 * node tree structure and do an operation
 * on him.
 * @author Vilim Staroveški
 *
 */
public interface INodeVisitor {

	/**
	 * Called when class visits a {@link TextNode}. 
	 * Does something with this node.
	 * @param node {@link TextNode} that is visited
	 */
	public void visitTextNode(TextNode node);
	/**
	 * Called when class visits a {@link ForLoopNode}. 
	 * Does something with this node.
	 * @param node {@link ForLoopNode} that is visited.
	 */
	public void visitForLoopNode(ForLoopNode node);
	/**
	 * Called when class visits a {@link EchoNode}. 
	 * Does something with this node.
	 * @param node {@link EchoNode} that is visited.
	 */
	public void visitEchoNode(EchoNode node);
	/**
	 * Called when class visits a {@link DocumentNode}. 
	 * Does something with this node.
	 * @param node {@link DocumentNode} that is visited.
	 */
	public void visitDocumentNode(DocumentNode node);
}
