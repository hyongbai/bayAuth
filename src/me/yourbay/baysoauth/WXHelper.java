package me.yourbay.baysoauth;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * @project sns_sample
 * @time 2013-8-8
 * @author ram
 */
public class WXHelper extends OauthHelper {

	public static String APP_ID;// ram@yunio:wx7caf9062644a2bb3,chris@yun.io:wx0bd8b4448730892c
	private IWXAPI api;

	public WXHelper(String ak, Activity activity) {
		super(ak, activity);
		APP_ID = ak;
		api = WXAPIFactory.createWXAPI(activity, APP_ID, true);
		api.registerApp(APP_ID);
	}

	// public WXHelper() {
	// }

	public void sendText(String content, boolean toMoments) {
		WXTextObject textObj = new WXTextObject();
		textObj.text = content;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// msg.title = "Will be ignored";
		msg.description = content;
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text");
		req.message = msg;
		req.scene = toMoments ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	public void sendWebPage(String url, Bitmap thumb, String title,
			String description, boolean toMoments) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		msg.thumbData = bmpToByteArray(thumb, false);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = toMoments ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);

	}

	public void sendVideo(String url, String title, String description,
			boolean toMoments) {
		WXVideoObject video = new WXVideoObject();
		video.videoLowBandUrl = url;

		WXMediaMessage msg = new WXMediaMessage(video);
		msg.title = title;
		msg.description = description;

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("video");
		req.message = msg;
		req.scene = toMoments ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	public void sendImage(Bitmap image, Bitmap thumb, String title,
			String description, boolean toMoments) {
		if (image == null) {
			return;
		}
		WXImageObject imgObj = new WXImageObject(image);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		msg.thumbData = bmpToByteArray(thumb, true);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = toMoments ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	/**
	 * NO OAUTH <br>
	 * NO NEED TO OAUTH
	 */
	public void oauth() {
	}

	@Override
	public String getProviderName() {
		return "wechat";
	}

	private static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		if (bmp == null) {
			return null;
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
}
