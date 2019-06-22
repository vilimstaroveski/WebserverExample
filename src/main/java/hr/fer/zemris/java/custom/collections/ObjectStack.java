package hr.fer.zemris.java.custom.collections;
/**
 * This class is representation of stack for storing objects. It uses implementation 
 * of {@code ArrayBackedIndexedCollection} through a Adaptor pattern where this class is Adaptor.
 * @author Vilim Staroveški
 * 
 */
public class ObjectStack {
	/**
	 * Private argument {@code ArrayBackedIndexedCollection} adaptee for Adaptor pattern
	 */
	private ArrayBackedIndexedCollection adaptee;
	/**
	 * Constructor that sets stack capacity on given {@code int} value 
	 * @param capacity - {@code int} value of capacity
	 */
	public ObjectStack(int capacity) {
		adaptee = new ArrayBackedIndexedCollection(capacity);
	}
	/**
	 * Constructor that sets stack capacity to default value 16
	 */
	public ObjectStack() {
		adaptee = new ArrayBackedIndexedCollection();
	}
	/**
	 * This method returns true if stack contains no objects and false otherwise.
	 * @return {@code boolean} value
	 */
	public boolean isEmpty() {
		return adaptee.isEmpty();
	}
	/**
	 * This method returns the number of currently stored objects on stack.
	 * @return {@code int} number of objects on stack
	 */
	public int size() {
		return adaptee.size();
	}
	/**
	 * This method adds the given object on stack.
	 * @param value - {@code Object} value we want to put on stack.
	 */
	public void push(Object value) {
		try{
			adaptee.add(value);
		} catch(IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}
	/**
	 * This method pops last element that was pushed on stack.
	 * @return {@code Object} object from stack.
	 */
	public Object pop() {
		Object value=null;
		value=peek();
		adaptee.remove(adaptee.size()-1);
		return value;
	}
	/**
	 * This method returns last element placed on stack but does not delete it from stack.
	 * @return element from stack
	 */
	public Object peek() {
		Object value=null;
		int indexOfLastElement = adaptee.size()-1;
		try {
			value = adaptee.get(indexOfLastElement);
		}catch(IndexOutOfBoundsException e) {
			System.err.println(e.getMessage());
			throw new EmptyStackException();
		}
		return value;
	}
	/**
	 * This method removes all elements from the stack
	 */
	public void clear() {
		adaptee.clear();
	}
}

