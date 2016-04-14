package martinutils.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import martinutils.runtime.Assert;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpRequestBuilder {

	private Map<String, String> queryParams = new HashMap<>();
	private Map<String, String> headers = new HashMap<>();
	private HttpClient client = HttpClientBuilder.create().build();

	/** Set a credentials for HTTP basic authentication. Returns instance for method chaining. */
	public HttpRequestBuilder withBasicAuthentication(String username, String password) {

		Assert.notNull(username, "username");
		Assert.notNull(username, "password");

		byte[] credentials = Base64.encodeBase64(
				String.format("%s:%s", username, password).getBytes()
		);
		setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		return this;
	}

	/** Set header */
	public HttpRequestBuilder setHeader(String name, String value) {
		headers.put(name, value);
		return this;
	}

	/**
	 * Add a query parameter to the request. Will be encoded and appended to URL if a GET request will be done,
	 * else it will be inside the request body if a POST will be done, either as form-data or x-www-form-urlencoded.
	 * @param name the parameter name
	 * @param value the parameter value
	 * @return instance for method chaining
	 */
	public HttpRequestBuilder addParam(String name, String value) {

		Assert.notEmpty(name, "name");
		Assert.notNull(value, "value");
		queryParams.put(name, value);
		return this;
	}

	/** Perform a get request to the specified URI */
	public HttpResponse get(String uri) throws IOException {

		HttpGet request = buildGetRequest(uri);
		HttpResponse response = client.execute(request);
		return response;
	}

	private HttpGet buildGetRequest(String uri) {

		HttpGet request = new HttpGet(uri);
		injectHeaders(request);
		return request;
	}

	private HttpPost buildPostRequest(String uri) {

		HttpPost request = new HttpPost(uri);
		injectHeaders(request);
		return request;
	}

	/**
	 * Do a post using all params passed to the builder. Warning, not implemented yet!
	 * @param uri the URI to which to POST
	 * @param urlEncoded if true the request will be x-www-form-urlencoded, else post-data
	 */
	public HttpResponse postForm(String uri, boolean urlEncoded) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/** Do a post with a json body */
	public HttpResponse postJson(String uri, String json) throws IOException {

		StringEntity entity = null;
		try {
			entity = new StringEntity(json);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		entity.setContentType("application/json");
		return postRaw(uri, entity);
	}

	/** Do a raw post by specifying the entity */
	public HttpResponse postRaw(String uri, HttpEntity entity) throws IOException {

		HttpPost request = buildPostRequest(uri);
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		return response;
	}

	private void injectHeaders(HttpRequest request) {

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			request.addHeader(entry.getKey(), entry.getValue());
		}
	}
}
