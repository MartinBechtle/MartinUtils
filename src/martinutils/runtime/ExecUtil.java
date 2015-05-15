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
	
	/**
	 * Stampa "Usage: className usage" in stderr e fa exit(-1)
	 * @param className
	 * @param usage
	 */
	public static void usage(String className, String usage)
	{
		String msg = String.format("Usage: %s %s", className, usage);
		die(msg);
	}
}
