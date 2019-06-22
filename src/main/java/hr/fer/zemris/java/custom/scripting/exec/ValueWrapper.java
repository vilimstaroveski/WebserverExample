package hr.fer.zemris.java.custom.scripting.exec;


/**
 * Class that stores a value that represents a number. Supported types are Integer, Double,
 * String(but number parsable) and null(parsed to Integer with value 0). 
 * 
 * @author Vilim Staroveški
 *
 */
public class ValueWrapper {
	
	/**
	 * Stored value.
	 */
	private Object value;
	/**
	 * Creates new {@link ValueWrapper}
	 * @param value that is going to be put inside
	 */
	public ValueWrapper(Object value) {
		
		this.value = value;
	}
	
	/**
	 * Adds stored value with value given as parameter.
	 * 
	 * @param incValue value that is added to stored value.
	 */
	public void increment(Object incValue) {
		
		NumberOperation operator = new NumberOperation(this.value, incValue) {
			
			@Override
			public Number doAnOperation() {
				TypeEnum typeOfResult = getFinalType();
				if(typeOfResult == TypeEnum.TYPE_INTEGER) {
					return getO1().intValue() + getO2().intValue();
				}
				else {
					return getO1().doubleValue() + getO2().doubleValue();
				}
			}
		};
		this.value = operator.doAnOperation();
	}
	
	/**
	 * Subtracts stored value with value given as parameter.
	 * 
	 * @param decValue value that is being subtracted from stored value.
	 */
	public void decrement(Object decValue) {

		NumberOperation operator = new NumberOperation(this.value, decValue) {
	
			@Override
			public Number doAnOperation() {
				TypeEnum typeOfResult = getFinalType();
				if(typeOfResult == TypeEnum.TYPE_INTEGER) {
					return getO1().intValue() - getO2().intValue();
				}
				else {
					return getO1().doubleValue() - getO2().doubleValue();
				}
			}
		};
		this.value = operator.doAnOperation();
	}
 
	/**
	 * Multiplies stored value with value given as parameter  and stores the result as new value.
	 * 
	 * @param mulValue value with the stored value is being multiplied.
	 */
	public void multiply(Object mulValue) {

		NumberOperation operator = new NumberOperation(this.value, mulValue) {
			
			@Override
			public Number doAnOperation() {
				TypeEnum typeOfResult = getFinalType();
				if(typeOfResult == TypeEnum.TYPE_INTEGER) {
					return getO1().intValue() * getO2().intValue();
				}
				else {
					return getO1().doubleValue() * getO2().doubleValue();
				}
			}
		};
		this.value = operator.doAnOperation();
	}
	
	/**
	 * Divides stored value with value given as parameter and stores it as a new value.
	 * 
	 * @param divValue value with the stored value is being divided by.
	 */
	public void divide(Object divValue) {

		NumberOperation operator = new NumberOperation(this.value, divValue) {
			
			@Override
			public Number doAnOperation() {
				TypeEnum typeOfResult = getFinalType();
				if(typeOfResult == TypeEnum.TYPE_INTEGER) {
					return getO1().intValue() / getO2().intValue();
				}
				else {
					return getO1().doubleValue() / getO2().doubleValue();
				}
			}
		};
		this.value = operator.doAnOperation();
	}
	
	/**
	 * Compares stored value to a value given as parameter. If stored value is greater, method will return 1
	 * if the values are equal, method returns 0 and if value is lesser, method will return -1.
	 * @param withValue value that the stored value is being compared to.
	 * @return 1 if stored value is greater, -1 if the stored value is lesser and 0 if the stored value is equal.
	 */
	public int numCompare(Object withValue) {
		
		NumberOperation operator = new NumberOperation(this.value, withValue) {
			
			@Override
			public Number doAnOperation() {
				return Double.compare(getO1().doubleValue(), getO2().doubleValue());
			}
		};
		return operator.doAnOperation().intValue();
	}

	/**
	 * Returns value stored in this {@link ValueWrapper}.
	 * @return value stored in this {@link ValueWrapper}.
	 */
	public Object getValue() {
	
		return this.value;
	}
	/**
	 * Sets new value in this {@link ValueWrapper}
	 * @param value new value1
	 */
	public void setValue(int value) {

		this.value = new Integer(value);
	}
	
}
