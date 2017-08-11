package com.mgilangjanuar.dev.goscele;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.mgilangjanuar.dev.goscele.Presenters.InAppBrowserPresenter;

import butterknife.BindView;

public class InAppBrowserActivity extends BaseActivity implements InAppBrowserPresenter.InAppBrowserServicePresenter {

    @BindView(R.id.toolbar_app_browser)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar_browser)
    ProgressBar progressBar;
    private InAppBrowserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_browser);

        presenter = new InAppBrowserPresenter(this, getIntent().getExtras().getString("url"));
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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (!presenter.isStoragePermissionGranted()) {
            webView.stopLoading();
            presenter.showStoragePermissionAlertDialog(InAppBrowserActivity.this);
        }

        CookieManager.getInstance().setAcceptCookie(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebViewClient(presenter.buildWebViewClient(toolbar, webView));
        webView.setWebChromeClient(presenter.buildWebChromeClient(progressBar));
        webView.setDownloadListener(presenter.buildDownloadListener());

        webView.clearCache(true);
        webView.clearHistory();
        presenter.clearCookies(this);

        CookieManager.getInstance().setCookie(presenter.url, presenter.authPresenter.getCookies());
        webView.loadUrl(presenter.url);
    }
}
