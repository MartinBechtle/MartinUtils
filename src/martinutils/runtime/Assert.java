package martinutils.runtime;

import java.util.Collection;

public class Assert {

	/**
	 * Throws an IllegalArgumentException telling "varName cannot be null", if the object passed is null
	 * @param o the object to check for null
	 * @param varName the name of the variable to print in the error msg
	 */
	public static void notNull(Object o, String varName) {
		if (o == null) throw new IllegalArgumentException(varName + " cannot be null");
	}
	
	/**
	 * Throws an IllegalArgumentException if the argument is null
	 * @param o the object to check for null
	 */
	public static void notNull(Object o) {
		if (o == null) throw new IllegalArgumentException();
	}

	/**
	 * Throws an IllegalArgumentException if the argument is null or empty String
	 * @param strToCheck the string to be checked
	 * @param varName the variable name to show in the exception message
	 */
	public static void notEmpty(String str, String varName) {
		if (str == null || str.isEmpty()) throw new IllegalArgumentException(varName + " cannot be empty");
	}
	
	/**
	 * Throws an IllegalArgumentException if the argument is null or empty String
	 * @param strToCheck
	 */
	public static void notEmpty(String str) {
		if (str == null || str.isEmpty()) throw new IllegalArgumentException();
	}
	
	/**
	 * Throws an IllegalArgumentException if the argument is empty or null
	 * @param c the collection to be checked
	 * @param varName the variable name to show in the exception message
	 */
	public static void notEmpty(Collection<?> c, String varName) {
		if (c == null || c.isEmpty()) throw new IllegalArgumentException(varName + " cannot be empty");
	}
	
	/**
	 * Throws an IllegalArgumentException if the argument is empty or null
	 * @param c the collection to be checked
	 */
	public static void notEmpty(Collection<?> c) {
		if (c == null || c.isEmpty()) throw new IllegalArgumentException();
	}
}
