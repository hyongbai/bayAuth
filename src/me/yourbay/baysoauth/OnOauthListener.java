package me.yourbay.baysoauth;

/**
 * @project bays-oauth
 * @time Aug 19, 2014
 * @author ram
 */
public interface OnOauthListener {
	public void onOauthSucceed(OauthHelper helper, Oauth oauth);

	public void onOauthFailed(OauthHelper helper, String provider, Object object);
}
