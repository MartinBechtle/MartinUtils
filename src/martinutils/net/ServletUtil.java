package martinutils.net;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {

	/**
	 * Returns the base URL for an http servlet request. Example: suppose your app is deployed on on https://mysite.com:8080/myapp.
	 * If the request is done to https://mysite.com:8080/myapp/page, this function will return https://mysite.com:8080/myapp
	 * @return
	 */
	public static String getBaseUrl(HttpServletRequest request) {

		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		String contextPath = request.getContextPath();

		if (port != 80) {
			return scheme + "://" + host + ":" + port + contextPath;
		}
		return scheme + "://" + host + contextPath;
	}
}
