package martinutils.runtime;

public class ExecUtil
{
	public static boolean isTrue(String str)
	{
		str = str.toLowerCase();
		return str.equals("true") || str.equals("1");
	}

	/**
	 * Print a message to sterr and quit with -1
	 * @param errMsg
	 */
	public static void die(String errMsg)
	{
		System.err.println(errMsg);
		System.exit(-1);
	}
}
