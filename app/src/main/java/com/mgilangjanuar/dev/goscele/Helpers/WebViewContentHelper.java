package com.mgilangjanuar.dev.goscele.Helpers;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mgilangjanuar.dev.goscele.InAppBrowserActivity;

/**
 * Created by mjanuar on 7/16/17.
 */

public class WebViewContentHelper {
    public static void setWebView(WebView webView, String content) {
        webView.loadDataWithBaseURL(null, "<style>body{margin:0;padding:0;}img{display: inline;height: auto;max-width: 100%;}</style>" + content, "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.getContext().startActivity((new Intent(webView.getContext(), InAppBrowserActivity.class)).putExtra("url", url));
                return true;
            }
        });
    }
}
