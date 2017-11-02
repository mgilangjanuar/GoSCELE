package com.mgilangjanuar.dev.goscele.modules.browser.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.mgilangjanuar.dev.goscele.base.BasePresenter;
import com.mgilangjanuar.dev.goscele.modules.browser.listener.BrowserListener;
import com.mgilangjanuar.dev.goscele.modules.browser.util.DownloadWrapper;
import com.mgilangjanuar.dev.goscele.modules.browser.util.WebViewClientUtil;
import com.mgilangjanuar.dev.goscele.modules.browser.view.BrowserActivity;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;
import com.mgilangjanuar.dev.goscele.utils.OpenOtherAppUtil;
import com.mgilangjanuar.dev.goscele.utils.ShareContentUtil;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class BrowserPresenter extends BasePresenter {

    public String url;
    private BrowserActivity activity;
    private BrowserListener listener;

    public BrowserPresenter(BrowserActivity activity, BrowserListener listener, String url) {
        this.activity = activity;
        this.listener = listener;
        this.url = url;
    }

    public boolean isContinueOpenWebView() {
        if (url.contains("&skipInspection=1")) {
            return true;
        }

//        if (url.contains("mod/forum/discuss.php?d=")) {
//            Intent intent = new Intent(activity, ForumDetailActivity.class).putExtra("url", url);
//            activity.startActivity(intent);
//            return false;
//        }
//
//        if (url.contains("mod/forum/view.php?")) {
//            Intent intent = new Intent(activity, ForumActivity.class).putExtra("url", url);
//            activity.startActivity(intent);
//            return false;
//        }
//
//        if (url.contains("course/view.php?id=") && isAlreadyEnrollCourse()) {
//            Intent intent = new Intent(activity, CourseDetailActivity.class).putExtra("url", url);
//            activity.startActivity(intent);
//            return false;
//        }

        return true;
    }

    public DownloadWrapper buildDownloadWrapper() {
        return new DownloadWrapper(activity, listener);
    }

    public WebViewClientUtil buildWebViewClient(Toolbar toolbar, WebView webView) {
        return new WebViewClientUtil(toolbar, webView);
    }

    public WebChromeClient buildWebChromeClient(final ProgressBar progressBar) {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                progressBar.setVisibility(newProgress < 100 ? ProgressBar.VISIBLE : ProgressBar.GONE);
            }
        };
    }

    public String getCookie() {
        CookieModel model = new CookieModel().find().where("key = ?", "MoodleSession").executeSingle();
        return "MoodleSession=" + model.value;
    }

    // source: https://stackoverflow.com/a/31950789
    @SuppressWarnings("deprecation")
    public void clearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public boolean isStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void showStoragePermissionAlertDialog(final Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            listener.onAskStoragePermission();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.ASK_STORAGE_PERMISSION_CODE);
        }
    }

    public void shareUrl(WebView webView) {
        ShareContentUtil.share(activity, webView.getUrl());
    }

    public void openOtherApp() {
        OpenOtherAppUtil.openLink(activity, url);
    }
}
