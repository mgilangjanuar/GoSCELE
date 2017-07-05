package com.mgilangjanuar.dev.goscele;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.mgilangjanuar.dev.goscele.Presenters.InAppBrowserPresenter;

public class InAppBrowserActivity extends BaseActivity implements InAppBrowserPresenter.InAppBrowserServicePresenter {

    WebView webView;
    InAppBrowserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_browser);

        webView = (WebView) findViewById(R.id.web_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("url") == null) {
            showToast("Broken URL");
            onBackPressed();
            return;
        }
        presenter = new InAppBrowserPresenter(this, bundle.getString("url"));
        setupInAppBrowser();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
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
                presenter.shareUrl(webView);
                break;
            case (R.id.item_browser_open):
                presenter.openOtherApp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupInAppBrowser() {
        if (!presenter.isContinueOpenWebView()) {
            super.onBackPressed();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_app_browser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            presenter.showStoragePermissionAlertDialog();
        }

        CookieManager.getInstance().setAcceptCookie(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(presenter.buildWebViewClient(toolbar, webView));
        webView.setWebChromeClient(presenter.buildWebChromeClient((ProgressBar) findViewById(R.id.progress_bar_browser)));

        webView.setDownloadListener(presenter.buildDownloadListener());

        webView.clearCache(true);
        webView.clearHistory();
        presenter.clearCookies(this);

        CookieManager.getInstance().setCookie(presenter.url, presenter.authPresenter.getCookies());
        webView.loadUrl(presenter.url);
    }
}
