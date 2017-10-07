package com.mgilangjanuar.dev.goscele.modules.browser.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class WebViewClientUtil extends WebViewClient {
    private Toolbar toolbar;
    private WebView webView;

    public WebViewClientUtil(Toolbar toolbar, WebView webView) {
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (url.contains("login/")) {
            UserModel model = new UserModel().find().executeSingle();
            if (model != null) {
                webView.evaluateJavascript("(function() {document.getElementsByName('username')[0].value='" + model.username + "';document.getElementsByName('password')[0].value='" + model.password + "';document.getElementById('login').submit(); " +
                        "return { var1: \"variable1\", var2: \"variable2\" }; })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("authentication", "success");
                    }
                });
            }
        }
    }
}
