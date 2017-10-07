package com.mgilangjanuar.dev.goscele.modules.browser.view;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.browser.listener.BrowserListener;
import com.mgilangjanuar.dev.goscele.modules.browser.presenter.BrowserPresenter;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class BrowserActivity extends BaseActivity implements BrowserListener {

    public AlertDialog alertDialog;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar_browser)
    ProgressBar progressBar;
    @BindView(R.id.toolbar_app_browser)
    Toolbar toolbar;
    private BrowserPresenter presenter;

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter = new BrowserPresenter(this, this, getIntent().getExtras().getString(Constant.URL));

        if (!presenter.isContinueOpenWebView()) {
            super.onBackPressed();
            return;
        }

        if (!presenter.isStoragePermissionGranted()) {
            webView.stopLoading();
            presenter.showStoragePermissionAlertDialog(BrowserActivity.this);
        }

        CookieManager.getInstance().setAcceptCookie(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebViewClient(presenter.buildWebViewClient(toolbar, webView));
        webView.setWebChromeClient(presenter.buildWebChromeClient(progressBar));
        webView.setDownloadListener(presenter.buildDownloadWrapper());

        webView.clearCache(true);
        webView.clearHistory();
        presenter.clearCookies(this);

        CookieManager.getInstance().setCookie(presenter.url, presenter.getCookie());
        webView.loadUrl(presenter.url);
    }

    @Override
    public int findLayout() {
        return R.layout.activity_browser;
    }

    @Override
    public Toolbar findToolbar() {
        return toolbar;
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
    public int findOptionsMenu() {
        return R.menu.in_app_browser_menu;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onAskStoragePermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.permission_title));
        builder.setMessage(getString(R.string.storage_permission));
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(BrowserActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.ASK_STORAGE_PERMISSION_CODE);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDownloadStart() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        finish();
    }
}
