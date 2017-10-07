package com.mgilangjanuar.dev.goscele.modules.browser.util;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.modules.browser.listener.BrowserListener;
import com.mgilangjanuar.dev.goscele.modules.browser.view.BrowserActivity;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class DownloadWrapper implements DownloadListener {

    private BrowserActivity activity;
    private BrowserListener listener;

    public DownloadWrapper(BrowserActivity activity, BrowserListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    // https://stackoverflow.com/questions/33434532/android-webview-download-files-like-browsers-do
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        listener.onDownloadStart();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setMimeType(mimetype);
        //------------------------COOKIE!!------------------------
        CookieManager.getInstance().setCookie(url, getCookie());
        String cookiesAlt = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader(Constant.COOKIE, cookiesAlt);
        //------------------------COOKIE!!------------------------
        request.addRequestHeader(Constant.USER_AGENT, userAgent);
        request.setDescription(activity.getString(R.string.downloading_file));
        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
        DownloadManager dm = (DownloadManager) activity.getSystemService(activity.DOWNLOAD_SERVICE);
        try {
            dm.enqueue(request);
            activity.showToast(R.string.downloading_file);
        } catch (Exception e) {
            activity.showToast(R.string.download_failed);
        }
    }

    private String getCookie() {
        CookieModel model = new CookieModel().find().where("key = ?", "MoodleSession").executeSingle();
        return "MoodleSession=" + model.value;
    }
}
