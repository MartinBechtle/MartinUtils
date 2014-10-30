package martinutils.runtime;

public class TestUtil
{
	public static void assertTrue(boolean b)
	{
		if (!b) throw new RuntimeException("test failed");
	}
}
