package com.mgilangjanuar.dev.goscele.utils;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mgilangjanuar.dev.goscele.modules.browser.view.BrowserActivity;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class WebViewContentUtil {

    public static void setWebView(final WebView webView, String content) {
        content = content.replace(" style=\"", " style\\=\"");
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadDataWithBaseURL(null, "<style>body{margin:0;padding:0;word-wrap:break-word;width:100%;}img{display:inline;height:auto;max-width:100%;}a{color:#F44336;}</style><body>" + content + "</body>", "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.getContext().startActivity((new Intent(webView.getContext(), BrowserActivity.class)).putExtra(Constant.URL, url));
                return true;
            }
        });
    }
}
