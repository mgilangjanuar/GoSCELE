package com.mgilangjanuar.dev.sceleapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mgilangjanuar.dev.sceleapp.Models.AccountModel;
import com.mgilangjanuar.dev.sceleapp.Presenters.AuthPresenter;

import java.util.Map;

public class InAppBrowser extends AppCompatActivity {

    WebView webView;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_browser);

        webView = (WebView) findViewById(R.id.web_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("url") == null) {
            Toast.makeText(this, "Broken URL", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        url = bundle.getString("url");

        if (url.contains("mod/forum/discuss.php?d=")) {
            Intent intent = new Intent(InAppBrowser.this, ForumDetail.class).putExtra("url", url);
            startActivity(intent);
            super.onBackPressed();
            return;
        } else if (url.contains("mod/forum/view.php?")) {
            Intent intent = new Intent(InAppBrowser.this, Forum.class).putExtra("url", url);
            startActivity(intent);
            super.onBackPressed();
            return;
        }  else if (url.contains("course/view.php?id=")) {
            Intent intent = new Intent(InAppBrowser.this, CourseDetail.class).putExtra("url", url);
            startActivity(intent);
            super.onBackPressed();
            return;
        }

        if (! (new AccountModel(this)).isUsingInAppBrowser()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            super.onBackPressed();
            return;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_app_browser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AuthPresenter authPresenter = new AuthPresenter(this);
        final Map<String, String> cookies = authPresenter.getCookies();

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setCookie(url, "MoodleSession=" + cookies.get("MoodleSession"));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
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
        });

        // https://stackoverflow.com/questions/33434532/android-webview-download-files-like-browsers-do
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                onBackPressed();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimetype);
                //------------------------COOKIE!!------------------------
                CookieManager.getInstance().setCookie(url, "MoodleSession=" + cookies.get("MoodleSession"));

                String cookiesAlt = CookieManager.getInstance().getCookie(url);
                Log.e("aiushausa", cookiesAlt);
                Log.e("aiushausaa", cookies.toString());
                request.addRequestHeader("cookie", "MoodleSession=" + cookies.get("MoodleSession"));
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });

        webView.loadUrl(url, cookies);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.in_app_browser_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.item_browser_share):
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                startActivity(Intent.createChooser(shareIntent, "Share Link"));
                break;
            case (R.id.item_browser_open):
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
