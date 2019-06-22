package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class that represents a {@link Map} of mapped stacks. Every stack has its name.
 * 
 * @author Vilim Staroveški
 *
 */
public class ObjectMultistack {

	/**
	 * Private {@link HashMap} for mapping {@link MultistackEntry's
	 */
	private HashMap<String, ObjectMultistack.MultistackEntry> stackMap;
	
	/**
	 * Creates a new {@link ObjectMultistack}.
	 */
	public ObjectMultistack() {
		
		stackMap = new HashMap<String, ObjectMultistack.MultistackEntry>();
	}
	
	/**
	 * Method that pushes given {@link ValueWrapper} on to a stack defined
	 * by name parameter.
	 * 
	 * @param name name of the stack on which this {@link ValueWrapper} will
	 * be added.
	 * @param valueWrapper value we want to add on stack.
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		
		MultistackEntry newOne = new MultistackEntry(name, valueWrapper);
		Optional<MultistackEntry> optional = Optional.ofNullable(stackMap.get(name));
		if(optional.isPresent()) {
			
			MultistackEntry oldest = optional.get().lastElement();
			oldest.next = newOne;
		}
		else {
			
			stackMap.put(name, newOne);
		}
	}
	
	/**
	 * Pops the last element that has been pushed to stack defined with 
	 * parameter name. If the name of stack is not present in this {@link ObjectMultistack}
	 * method will throw an exception.
	 * 
	 * @param name name of the stack where we want to pop element.
	 * @return popped element as {@link ValueWrapper} value.
	 * @throws NonExistingStackException if the name of stack is not present in this {@link ObjectMultistack}
	 */
	public ValueWrapper pop(String name) {
		
		Optional<MultistackEntry> optional = Optional.ofNullable(stackMap.get(name));
		if(optional.isPresent()) {
			
			MultistackEntry secondLast = null;
			MultistackEntry temp = optional.get();
			while(temp.next != null) {
				
				secondLast = temp;
				temp = temp.next;
			}
			if(secondLast == null) {
				
				stackMap.remove(name);
			}
			else {
				
				secondLast.next = null;
			}
			return temp.value;
		}
		else {
			
			throw new NonExistingStackException("You requested poping from the stack that does not exist!");
		}
	}
	
	/**
	 * Returns last element as {@link ValueWrapper} added to stack defined
	 * by name but does not removes it from the stack. 
	 * If the name of stack is not present in this {@link ObjectMultistack}
	 * method will throw an exception.
	 * 
	 * @param name name of the stack from which we take element.
	 * @return  last element as {@link ValueWrapper} added to stack
	 * @throws NonExistingStackException if the name of stack is not present in this {@link ObjectMultistack}
	 */
	public ValueWrapper peek(String name) {
		
		Optional<MultistackEntry> optional = Optional.ofNullable(stackMap.get(name));
		if(optional.isPresent()) {
			
			return optional.get().lastElement().value;
		}
		else {
			
			throw new NonExistingStackException("The stack from which the peek is wanted, does not exists!");
		}
	}
	
	/**
	 * Returns true if there are no elements in stack defined by name. 
	 * @param name name of the stack.
	 * @return true if there are no elements in stack defined by name. False otherwise.
	 */
	public boolean isEmpty(String name) {
		
		Optional<MultistackEntry> optional = Optional.ofNullable(stackMap.get(name));
		return !optional.isPresent();
	}
	
	/**
	 * Returns {@link HashMap} that maps {@link MultistackEntry} in this {@link ObjectMultistack}.
	 * @return {@link HashMap} that maps {@link MultistackEntry} in this {@link ObjectMultistack}
	 */
	public HashMap<String, ObjectMultistack.MultistackEntry> getHashMap() {
		
		return this.stackMap;
	}
	
	/**
	 * Class that represents a stack structure that can add instances of
	 * {@link ValueWrapper}.
	 * 
	 * @author Vilim Staroveški
	 *
	 */
	protected static class MultistackEntry {
		
		/**
		 * Reference to the {@link MultistackEntry} that is added to the 
		 * stack after this {@link MultistackEntry}.
		 */
		private MultistackEntry next;
		
		/**
		 * Value that is stored in this {@link MultistackEntry}.
		 */
		private ValueWrapper value;
		
		/**
		 * Name of this {@link MultistackEntry}.
		 */
		private String name;
		/**
		 * Creates new {@link MultistackEntry}
		 * @param name name of stack
		 * @param value {@link ValueWrapper}
		 */
		public MultistackEntry(String name, ValueWrapper value) {
			
			next = null;
			this.name = name;
			this.value = value;
		}
		
		/**
		 * Returns the element that is last in the list where this is one 
		 * node.
		 * 
		 * @return last element from the list of {@link MultistackEntry}'s.
		 */
		public MultistackEntry lastElement() {
			
			MultistackEntry oldest = this;
			while(oldest.next != null ) {
				
				oldest = oldest.next;
			}
			return oldest;
		}
	}
}
