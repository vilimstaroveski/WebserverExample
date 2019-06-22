package hr.fer.zemris.java.custom.collections;

/**
 * This class implementation of resizable array-backed collection of objects. Duplicate elements are allowed and
 * null references are not allowed.
 * @author Vilim Staroveški
 * 
 */
public class ArrayBackedIndexedCollection {
	/**
	 * Current size of collection (number of elements actually stored)
	 */
	private int size;
	/**
	 * Current capacity of allocated array of object references
	 */
	private int capacity;
	/**
	 * An array of object references which length is determined by capacity variable
	 */
	private Object[] elements;
	/**
	 * This constructor creates an instance {@code ArrayBackedIndexedCollection} with capacity set to
	 * 16
	 */
	public ArrayBackedIndexedCollection() {
		this(16);
	}
	/**
	 * This constructor have single integer parameter: {@code initialCapacity} and sets the capacity to that
     * value
	 * @param initialCapacity - Wanted initial capacity of the collection.
	 * 
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) throw new IllegalArgumentException("Argument initialCapacity is less than 1.");
		else {
			capacity = initialCapacity;
			size = 0;
			elements = new Object[capacity];
		}
	}
	/**
	 * This method returns true if collection contains no objects and false otherwise.
	 * @return {@code boolean} value
	 */
	public boolean isEmpty() {
		if(size == 0 && elements.length==0) return true;
		return false;
	}
	/**
	 * This method returns the number of currently stored objects in collection.
	 * @return {@code int} value
	 */
	public int size() {
		return size;
	}
	/**
	 * This method adds the given object into the collection,
	 * if the elements array is full, it should be reallocated by doubling its size.
	 * @param value - {@code Object} value we want to put in collection.
	 * @throws IllegalArgumentException if we try to add null value in collection.
	 */
	public void add(Object value) {
		if(value == null) throw new IllegalArgumentException("You are trying to add null value in collection.");
		if(size+1 > capacity) {
			elements = doublingArray(elements);
			elements[size++] = value;
			capacity = elements.length;
		}
		else {
			elements[size++] = value;
		}
	}
	/**
	 * This method doubles capacity of collection.
	 * @param oldArray - {@code Object} array we want to double its capacity.
	 * @return {@code Object} array with doubled capacity.
	 */
	private static Object[] doublingArray(Object[] oldArray) {
		Object[] newArray= new Object[oldArray.length*2];
		for(int i=0; i<oldArray.length; i++) {
			newArray[i] = oldArray[i];
		}
		return newArray;
	}
	/**
	 * This method returns the object that is stored in backing array at position index.
	 * @param index - {@code int} index of wanted object.
	 * @return {@code Object} value from collection on given index.
	 * @throws IndexOutOfBoundsException if index is not in range from 0 to (collection size-1)
	 */
	public Object get(int index) {
		if(indexValidator(index)) throw new IndexOutOfBoundsException();
		return elements[index];
	}
	/**
	 * This method removes the object that is stored in the backing array at position index.
	 * @param index - index of element we want to remove
	 * @throws IndexOutOfBoundsException if index is not in range from 0 to (collection size-1)
	 */
	public void remove(int index) {
		if(indexValidator(index)) throw new IndexOutOfBoundsException();
		for(int i=index; i<size-1; i++) {
			elements[i] = elements[i+1];
		}
		--size;
	}
	/**
	 * This method inserts (does not overwrite) the given value at the given position in array.
	 * @param value - {@code Object} value we want to put in collection
	 * @param position - {@code int} value of index in collection where we want to put new element
	 * @throws IndexOutOfBoundsException if position is not in range from 0 to (collection size-1)
	 */
	public void insert(Object value, int position) {
		if(indexValidator(position)) throw new IndexOutOfBoundsException();
		this.add(0);
		for(int i=size-1; i>position; i--) {
			elements[i] = elements[i-1];
		}
		elements[position] = value;
	}
	/**
	 * This method returns true if index is in range from 0 to (collection size-1), false otherwise
	 * @param index - index that is checked
	 * @return {@code boolean} value
	 */
	private boolean indexValidator(int index) {
		return index<0 || index>size;
	}
	/**
	 * This method returns the index of the first occurrence of the given value or -1 if the value is not found
	 * @param value - {@code Object} value that is searched
	 * @return index of given {@code Object}
	 */
	public int indexOf(Object value) {
		for(int i=0; i<size; i++) {
			if(elements[i].equals(value)) return i;
		}
		return -1;
	}
	/**
	 * This method returns true only if the collection contains given {@code Object} value.
	 * @param value - searched object
	 * @return {@code boolean} value
	 */
	public boolean contains(Object value) {
		if(this.indexOf(value) != -1) return true;
		return false;
	}
	/**
	 * This method removes all elements from the collection
	 */
	public void clear() {
		elements = new Object[elements.length];
		size = 0;
	}
}
