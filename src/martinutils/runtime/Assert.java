package martinutils.runtime;

public class Assert {

	/**
	 * Throws an IllegalArgumentException telling "varName cannot be null", if the object passed is null
	 * @param o the object to check for null
	 * @param nvarName the name of the variable to print in the error msg
	 */
	public static void notNull(Object o, String varName) {
		if (o == null) throw new IllegalArgumentException(varName + " cannot be null");
	}
	
	/**
	 * Throws an IllegalArgumentException if the argument is null
	 */
	public static void notNull(Object o) {
		if (o == null) throw new IllegalArgumentException();
	}

	/**
	 * Throws an IllegalArgumentException if the argument is null or empty String
	 */
	public static void notEmpty(String str) {
		if (str == null || str.isEmpty()) throw new IllegalArgumentException();
	}
}
