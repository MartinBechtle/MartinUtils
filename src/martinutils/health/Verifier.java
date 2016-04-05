package martinutils.health;

import java.io.IOException;

import martinutils.net.HttpRequestBuilder;

public class Verifier {

	private HttpRequestBuilder builder;

	public Verifier() {
		builder = new HttpRequestBuilder();
	}

	public void verify(String a, String b) {
		builder.withBasicAuthentication(a, b);
	}

	public void hashCheck(String hash, String key) throws IOException {
		builder.postJson(hash, key);
	}
}