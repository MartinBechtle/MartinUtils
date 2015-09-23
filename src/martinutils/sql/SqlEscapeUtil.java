package martinutils.sql;

public class SqlEscapeUtil {

	/**
	 * Adds slashes to all single quotes in a String
	 * @param str The string to escape
	 * @return The escaped string
	 */
	public static String escapeSingleQuotes(String str) {
		return str.replace("'", "\\'");
	}

	/**
	 * Escapes ' " and \ characters with backslashes
	 * @param str
	 * @return
	 */
	public static String escapeSQL(String str) {
		return str.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
	}

	/**
	 * Adds slashes to all \ % and _ characters, used in the MySQL Like Operator. Does not treat quotes!
	 * @param str The string to escape
	 * @return The escaped string
	 */
	public static String escapeForLikeOp(String str) {
		return str.replace("%", "\\%").replace("_", "\\_");
	}
	
	/**
	 * Adds slashes to all \ % and _ characters, used in the MySQL Like Operator, then encloses in %...%. Does not treat quotes!
	 * @param str The string to escape
	 * @return The escaped string
	 */
	public static String escapeForLikeSubOp(String str)
	{
		str = str.replace("%", "\\%").replace("_", "\\_");
		return "%" + str + "%";
	}
}
