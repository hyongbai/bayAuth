package me.yourbay.baysoauth;

import android.app.Activity;
import android.content.Intent;

/**
 * @time 2013-8-13
 * @author ram
 */
public abstract class OauthHelper {
	protected String TAG = "OauthHelper";
	protected boolean DEBUG = false;
	protected Activity mActivity;

	public abstract String getProviderName();

	private OnOauthListener mOauthListener = null;

	public void setOauthListener(OnOauthListener listener) {
		mOauthListener = listener;
	}

	public OauthHelper(String ak, Activity activity) {
		this.mActivity = activity;
	}

	public abstract void oauth();

	public void oauthSucceed(Oauth oauth) {
		if (mOauthListener != null) {
			mOauthListener.onOauthSucceed(this, oauth);
		}
	}

	public void oauthFailed(Object object) {
		if (mOauthListener != null) {
			mOauthListener.onOauthFailed(this, getProviderName(), object);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	// public final static void storeOauth(Oauth oauth) {
	// // mSimpleData.putString(Const.SSO_TYPE, oauth.getType());
	// // mSimpleData.putString(Const.SSO_TOKEN, oauth.getToken());
	// }
	//
	// public final static Oauth getOauth() {
	// // String provider = mSimpleData.getString(Consts.TYPE, null);
	// // if (provider == null) {
	// // return null;
	// // }
	// // String token = mSimpleData.getString(Consts.TOKEN, null);
	// // if (token == null) {
	// // return null;
	// // }
	// // return new Oauth(provider, token);
	// return null;
	// }
	//
	// public final static void clearOAuth() {
	// // mSimpleData.remove(Const.SSO_TYPE);
	// // mSimpleData.remove(Const.SSO_TOKEN);
	// }

}
