package me.yourbay.baysoauth;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * @project Pickup
 * @time 2013-11-13
 * @author ram
 */
public class QQHelper extends OauthHelper {

	private Tencent mTencent;
	private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
	private final String APP_KEY;
	private IUiListener listener = new IUiListener() {
		@Override
		public void onCancel() {
			oauthFailed(null);
		}

		@Override
		public void onError(UiError uiError) {
			oauthFailed(uiError);
			if (DEBUG) {
				Log.d(TAG, "onComplete()  UiError " + uiError.errorCode
						+ " errorMessage=" + uiError.errorMessage
						+ " errorDetail=" + uiError.errorDetail);
			}
		}

		@Override
		public void onComplete(Object arg0) {
			if (DEBUG) {
				Log.d(TAG, "onComplete()  arg0 is " + arg0);
			}
			try {
				parserToken((JSONObject) arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public QQHelper(String ak, Activity activity) {
		super(ak, activity);
		APP_KEY = ak;
		mTencent = Tencent.createInstance(APP_KEY, mActivity);
	}

	@Override
	public String getProviderName() {
		return "qq";
	}

	@Override
	public void oauth() {
		if (mTencent == null) {
			oauthFailed(null);
			return;
		}
		if (DEBUG) {
			Log.d(TAG, "oauth!!!!!!!!!!!!");
		}
		try {
			if (mActivity instanceof Activity) {
				Activity activity = mActivity;
				mTencent.logout(activity);
				mTencent.login(activity, "all", listener);
				// mTencent.loginWithOEM(activity, "all", listener, "10000144",
				// "10000144", "xxxx");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mTencent != null) {
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("SdCardPath")
	public void shareToQQSpace(String title, String summary, String targetURL,
			String imageUrl) {// String id
		try {
			Bundle bundle = new Bundle();
			bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
			bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
					targetURL);
			bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
			bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
			bundle.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
			// "/sdcard/eclipse_logo_colour.png");
			// "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
			mTencent.shareToQQ(mActivity, bundle, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parserToken(JSONObject arg0) {
		try {
			String token = arg0.getString("access_token");
			Oauth oauth = new Oauth(getProviderName(), token);
			if (oauth.isValid()) {
				oauthSucceed(oauth);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		oauthFailed(null);
	}

}
