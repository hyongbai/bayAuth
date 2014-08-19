package me.yourbay.baysoauth;

import android.text.TextUtils;

/**
 * @project Pickup
 * @time 2013-11-13
 * @author ram
 */
public class Oauth {
	private String token;
	private String provider;

	public Oauth(String provider, String token) {
		this.token = token;
		this.provider = provider;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getProvider() {
		return provider;
	}

	public void setgetProvider(String provider) {
		this.provider = provider;
	}

	public boolean isValid() {
		if (provider != null && !TextUtils.isEmpty(token)) {
			token = token.trim();
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[provider:" + provider + "] \n [token:" + token + "]";
	}

}
