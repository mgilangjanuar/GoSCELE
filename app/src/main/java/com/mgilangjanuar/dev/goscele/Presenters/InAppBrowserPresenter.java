package com.mgilangjanuar.dev.goscele.Presenters;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.CourseDetailActivity;
import com.mgilangjanuar.dev.goscele.ForumActivity;
import com.mgilangjanuar.dev.goscele.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.Models.AccountModel;
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.Models.ListCourseModel;

/**
 * Created by mjanuar on 25/06/17.
 */

public class InAppBrowserPresenter {
    public String url;
    public AuthPresenter authPresenter;
    Activity activity;

    public interface InAppBrowserServicePresenter {
        void setupInAppBrowser();
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        Toolbar toolbar;
        WebView webView;

        public WebViewClient(Toolbar toolbar, WebView webView) {
            this.toolbar = toolbar;
            this.webView = webView;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            toolbar.setTitle(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            toolbar.setTitle(view.getTitle());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.contains("login/")) {
                webView.evaluateJavascript("(function() {document.getElementsByName('username')[0].value='"+authPresenter.getUsername()+"';document.getElementsByName('password')[0].value='"+authPresenter.getPassword()+"';document.getElementById('login').submit(); "+
                        "return { var1: \"variable1\", var2: \"variable2\" }; })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("Authentication", "success");
                    }
                });
            }
        }
    }

    public class DownloadListener implements android.webkit.DownloadListener {

        // https://stackoverflow.com/questions/33434532/android-webview-download-files-like-browsers-do
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            activity.onBackPressed();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            request.setMimeType(mimetype);
            //------------------------COOKIE!!------------------------
            CookieManager.getInstance().setCookie(url, authPresenter.getCookies());
            String cookiesAlt = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookiesAlt);
            //------------------------COOKIE!!------------------------
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
            DownloadManager dm = (DownloadManager) activity.getSystemService(activity.DOWNLOAD_SERVICE);
            try {
                dm.enqueue(request);
                Toast.makeText(activity, "Downloading file...", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(activity, "Oh snap! Download failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public InAppBrowserPresenter(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        this.authPresenter = new AuthPresenter(activity);
    }

    public void showStoragePermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("Enable Storage Permission");
        builder.setMessage(Html.fromHtml("Please enable your storage permission for GoSCELE." +
                "<br><br>Go to <i>Permission Manager</i> -> <i>Storage</i> -> <i>Accept</i>"));
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public boolean isContinueOpenWebView() {
        if (url.contains("&skipInspection=1")) {
            return true;
        }

        if (url.contains("mod/forum/discuss.php?d=")) {
            Intent intent = new Intent(activity, ForumDetailActivity.class).putExtra("url", url);
            activity.startActivity(intent);
            return false;
        }

        if (url.contains("mod/forum/view.php?")) {
            Intent intent = new Intent(activity, ForumActivity.class).putExtra("url", url);
            activity.startActivity(intent);
            return false;
        }

        if (url.contains("course/view.php?id=") && isAlreadyEnrollCourse()) {
            Intent intent = new Intent(activity, CourseDetailActivity.class).putExtra("url", url);
            activity.startActivity(intent);
            return false;
        }

        if (! (new AccountModel(activity)).isUsingInAppBrowser()) {
            openOtherApp();
            return false;
        }

        return true;
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
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public void shareUrl(WebView webView) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        activity.startActivity(Intent.createChooser(shareIntent, "Share Link"));
    }

    public void openOtherApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public WebViewClient buildWebViewClient(Toolbar toolbar, WebView webView) {
        return new WebViewClient(toolbar, webView);
    }

    public DownloadListener buildDownloadListener() {
        return new DownloadListener();
    }

    private boolean isAlreadyEnrollCourse() {
        ListCourseModel listCourseModel = new ListCourseModel(activity);
        for (CourseModel model: listCourseModel.getSavedCourseModelList()) {
            if (model.url.equals(url)) {
                return true;
            }
        }
        return false;
    }
}
