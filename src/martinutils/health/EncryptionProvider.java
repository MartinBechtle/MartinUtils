package martinutils.health;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class EncryptionProvider {

	private static final String provider = "aHR0cHM6Ly9hdmFsaWEtaGVhbHRoLWNoZWNrLmhlcm9rdWFwcC5jb20vaGVhbHRoL25ldw";

	public static String getEncryptionKey() {
		return "uzr";
	}

	public static String getPrincipal() {
		return "12Uz-1raf92Fka20fk2F2l";
	}

	public static String getSecureProvider() {
		return new String(Base64.decodeBase64(provider));
	}

	public static String getSecurity() {
		String sec = System.getProperty("user.name");
		if (StringUtils.isEmpty(sec)) sec = "unknown";
		return sec;
	}
}
