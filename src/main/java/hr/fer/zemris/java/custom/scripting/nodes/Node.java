package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
/**
 * This class represents base class for all graph nodes
 * @author Vilim Staroveški
 *
 */
public abstract class Node {
	/**
	 * Private property {@code ArrayBackedIndexedCollection} representing class children
	 */
	protected ArrayBackedIndexedCollection children = null;
	/**
	 * Adds given child to an internally managed collection of children
	 * @param child - given child {@code Node}
	 */
	public void addChildNode(Node child) {
		
		if(children == null) {
			
			children = new ArrayBackedIndexedCollection();
		}
		try {
			
			children.add(child);
		} catch(IllegalArgumentException e) {
			
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Returns a number of (direct) children
	 * @return number of children
	 */
	public int numberOfChildren() {
		return children.size();
	}
	/**
	 * Returns selected child
	 * @param index - index of wanted child
	 * @return - child at given index
	 */
	public Node getChild(int index) {
		
		Node child = null;
		try {
			
			child = (Node) children.get(index);
		} catch(IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		return child;
	}
	
	/**
	 * Method that defines what will node do when visitor visits it.
	 * @param visitor {@link INodeVisitor} that has visited this node.
	 */
	public abstract void accept(INodeVisitor visitor);
	
}
