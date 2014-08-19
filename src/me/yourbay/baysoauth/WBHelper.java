package me.yourbay.baysoauth;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * @project sns_sample
 * @time 2013-8-8
 * @author ram
 */
public class WBHelper extends OauthHelper {

	protected final static String TAG = "WBHelper";
	private String REDICTURL;
	private final String AK;
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	private SsoHandler mSsoHandler;
	private WeiboAuth weiboAuth;
	private IWeiboShareAPI weiboShareAPI;

	private WeiboAuthListener weiboAuthListener = new WeiboAuthListener() {

		@Override
		public void onComplete(Bundle values) {
			try {
				Oauth2AccessToken mAccessToken = Oauth2AccessToken
						.parseAccessToken(values);
				if (DEBUG) {
					Log.d(TAG, "onComplete()	" + mAccessToken.getToken() + "	"
							+ mAccessToken.isSessionValid());
				}
				if (mAccessToken.isSessionValid()) {
					Oauth oauth = new Oauth(getProviderName(),
							mAccessToken.getToken());
					if (oauth.isValid()) {
						oauthSucceed(oauth);
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Exception:" + e);
			}
			oauthFailed(values);
		}

		@Override
		public void onCancel() {
			oauthFailed(null);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e(TAG, "Auth exception : " + e.getMessage());
			oauthFailed(null);
		}
	};

	public WBHelper(String ak, Activity activity) {
		super(ak, activity);
		AK = ak;
	}

	public void setRedictUrl(String url) {
		REDICTURL = url;
	}

	@Override
	public String getProviderName() {
		return "weibo";
	}

	@Override
	public void oauth() {
		weiboAuth = new WeiboAuth(mActivity, AK, REDICTURL, SCOPE);
		mSsoHandler = new SsoHandler(mActivity, weiboAuth);
		mSsoHandler.authorize(weiboAuthListener);

	}

	public void sendText(String text) {
		TextObject textObject = new TextObject();
		textObject.text = (String) text;
		sendIt(textObject);
	}

	/**
	 * @param object
	 *            should be a drawable or bitmap or localpath
	 */
	public void sendImg(Object object) {
		sendImg(object, null);
	}

	/**
	 * @param object
	 *            should be a drawable or bitmap or localpath
	 */
	public void sendImg(Object object, String title) {
		if (object == null)
			return;
		try {
			Bitmap bitmap = null;
			if (object instanceof Drawable) {
				bitmap = ((BitmapDrawable) object).getBitmap();
			} else if (object instanceof Bitmap) {
				bitmap = (Bitmap) object;
			} else if (object instanceof String) {
				bitmap = BitmapFactory.decodeFile(object.toString());
			} else {
				return;
			}
			ImageObject imageObject = new ImageObject();
			imageObject.setImageObject(bitmap);

			WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
			weiboMessage.imageObject = imageObject;
			if (title != null) {
				TextObject textObject = new TextObject();
				textObject.text = title;
				weiboMessage.textObject = textObject;
			}

			SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
			/* transaction is a unique string */
			req.transaction = UUID.randomUUID().toString()
					+ System.currentTimeMillis();
			req.multiMessage = weiboMessage;
			sendReuest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void sendIt(BaseMediaObject object) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.mediaObject = object;
		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		/* transaction is a unique string */
		req.transaction = UUID.randomUUID().toString()
				+ System.currentTimeMillis();
		req.multiMessage = weiboMessage;
		sendReuest(req);
	}

	private void sendReuest(SendMultiMessageToWeiboRequest req) {
		init();
		weiboShareAPI.sendRequest(req);
	}

	private void init() {
		if (weiboShareAPI != null) {
			return;
		}
		weiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, AK);
		weiboShareAPI.registerApp();
	}
}
