package hr.fer.zemris.java.custom.scripting.exec;

import java.security.InvalidParameterException;

/**
 * Class that is used to perform operations on given {@link Object} values. If the values are not representations
 * of numbers an exceptions will be thrown.
 * 
 * @author Vilim Staroveški
 *
 */
public abstract class NumberOperation {

	/**
	 * Object that will be used in this class for arithmetic operations.
	 */
	private final Object firstObject;
	
	/**
	 * Type of the first object.
	 */
	private final TypeEnum typeOfFirstObject;
	
	/**
	 * Object that will be used in this class for arithmetic operations.
	 */
	private final Object secondObject;
	
	/**
	 * Type of the second object.
	 */
	private final TypeEnum typeOfSecondObject;
	
	/**
	 * Returns a {@link Number} representing a result of operation defined in this method.
	 * @return {@link Number} representing a result of operation defined in this method.
	 */
	public abstract Number doAnOperation();
		
	/**	
	 * Creates a new {@link NumberOperation} with given parameters as new private variables.
	 * @param o1 object that will be used in this class.
	 * @param o2 object that will be used in this class.
	 */
	public NumberOperation(Object o1, Object o2) {
	
		this.firstObject = validateValue(o1);
		this.typeOfFirstObject = tellType(o1);
		this.secondObject = validateValue(o2);
		this.typeOfSecondObject = tellType(o2);
	}
		
	/**
	 * Returns predicted type of a result if private objects are used in the same arithmetic operation. 
	 * @return predicted type of a result if private objects are used in the same arithmetic operation. 
	 */
	public TypeEnum getFinalType() {
		
		if((typeOfFirstObject == TypeEnum.TYPE_INTEGER || typeOfFirstObject == TypeEnum.TYPE_STRING_INTEGER) 
				&& (typeOfSecondObject == TypeEnum.TYPE_INTEGER || typeOfSecondObject == TypeEnum.TYPE_STRING_INTEGER)) {
			
			return TypeEnum.TYPE_INTEGER;
		}
		else {
			return TypeEnum.TYPE_DOUBLE;
		}
	}
	
	/**
	 * Returns first {@link Object} that is used in this operation class.
	 * @return first {@link Object} that is used in this operation class.
	 */
	public Number getO1() {
		return (Number)firstObject;
	}

	/**
	 * Returns second {@link Object} that is used in this operation class.
	 * @return second {@link Object} that is used in this operation class.
	 */
	public Number getO2() {
		return (Number)secondObject;
	}


	/**
	 * Evaluates value and performs additional operation if the type of value is
	 * not compatible for this class.
	 * @param value value given as parameter.
	 * @return evaluated value.
	 * @throws InvalidParameterException if the value is not a number representation.
	 */
	private Number validateValue(Object value) {
		
		switch(tellType(value)) {
		
			case TYPE_NULL: 			return new Integer(0);
	
			case TYPE_STRING_INTEGER: 	return parseToNumber((String)value);
			
			case TYPE_STRING_DOUBLE:	return parseToNumber((String)value);
	
			case TYPE_DOUBLE:			return (Double)value;
			
			case TYPE_INTEGER:			return (Integer)value;
			
			default:	 				throw new InvalidParameterException("Given parameter "+value.getClass().getName()+" is not compatible for arithemtic operations!");
		}
	}

	/**
	 * Parses String in to a Number. If String is not parsable method will print the error messege to error output.
	 * @param value String value.
	 * @return Number value representing a parameter String.
	 */
	private Number parseToNumber(String value) {

		if(value.contains(".")) {
			Double num = 0.0;
			try {
				
				num = Double.parseDouble(value);
			} catch(NumberFormatException e) {
				
				System.out.println("String is not a representation of a number!");
			}
			
			return num;
		}
		else {
			Integer num = 0;
			try {
				
				num = Integer.parseInt(value);
				} catch(NumberFormatException e) {
					
					System.out.println("String is not a representation of a number!");
				}
			
			return num;
		}
	}

	/**
	 * Evaluates given {@link Object} by type. Throws an exception if the type of the value is not compatible 
	 * for this class.
	 * @param value {@link Object} value which type is being determined.
	 * @return {@link TypeEnum} which tells what type {@link Object} value is.
	 * {@link TypeEnum#TYPE_DOUBLE} if the value is instance of Double, {@link TypeEnum#TYPE_INTEGER} if 
	 * the value is instance of Integer, {@link TypeEnum#TYPE_STRING_INTEGER} if the value is instance of String that
	 * represents an Integer, {@link TypeEnum#TYPE_STRING_DOUBLE} if the value is instance of String that
	 * represents an Double and {@link TypeEnum#TYPE_NULL} if the value is null. Otherwise {@link TypeEnum#TYPE_OTHER}
	 */
	private TypeEnum tellType(Object value) {

    	if(value == null) {
   			
   			return TypeEnum.TYPE_NULL;
   		}
   		if(value instanceof Integer) {
   			
    		return TypeEnum.TYPE_INTEGER;
    	}
   		if(value instanceof Double || value instanceof Float) {
   			
   			return TypeEnum.TYPE_DOUBLE;
   		}
    	if(value instanceof String) {
    		
    		if(((String) value).contains(".")) {
    			return TypeEnum.TYPE_STRING_DOUBLE;
    		}
    		else {
    			return TypeEnum.TYPE_STRING_INTEGER;
    		}
   		}
   		else {
    		
   			return TypeEnum.TYPE_OTHER;
   		}
   	}
}
