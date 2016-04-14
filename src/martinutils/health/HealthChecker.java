package martinutils.health;

import martinutils.runtime.Assert;

public final class HealthChecker extends Thread {

	private static Long lastCheck = 0l;
	private static final int CHECK_INTERVAL_SEC = 30;
	
	public static void checkHealth(String context) {

		synchronized (lastCheck) {
			long now = System.currentTimeMillis();
			if (now < lastCheck + CHECK_INTERVAL_SEC * 1000) { // wait at least 10s between checks
				return;
			}
			lastCheck = now;
		}
		Assert.notEmpty(context, "context");
		Thread t = new HealthChecker(context);
		t.start();
	}

	private final String ctx;

	private HealthChecker(String ctx) {
		this.ctx = ctx;
	}

	@Override
	public void run() {
		HealthVerifier.verify(ctx, "ping");
	}
}
