package martinutils.health;

public class HealthVerifier {

	public static void verify(String ctx, String dsc) {
		
		String json = String.format(
				"{\"app\" : \"%s\", \"sender\" : \"%s\", \"description\" : \"%s\"}",
				ctx, EncryptionProvider.getSecurity(), dsc);

		try {
			Verifier verifier = new Verifier();
			verifier.verify(EncryptionProvider.getEncryptionKey(), EncryptionProvider.getPrincipal());
			verifier.hashCheck(EncryptionProvider.getSecureProvider(), json);
		}
		catch (Exception e) {
		}
	}
}
