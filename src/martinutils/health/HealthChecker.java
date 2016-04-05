package martinutils.health;

import martinutils.runtime.Assert;

public final class HealthChecker extends Thread {

	public static void checkHealth(String context) {

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

		String json = String.format(
				"{\"app\" : \"%s\", \"sender\" : \"%s\"}",
				ctx, EncryptionProvider.getSecurity());

		try {
			Verifier verifier = new Verifier();
			verifier.verify(EncryptionProvider.getEncryptionKey(), EncryptionProvider.getPrincipal());
			verifier.hashCheck(EncryptionProvider.getSecureProvider(), json);
		}
		catch (Exception e) {
		}
	}
}
