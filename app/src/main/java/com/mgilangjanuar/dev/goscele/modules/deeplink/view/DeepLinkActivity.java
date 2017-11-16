package com.mgilangjanuar.dev.goscele.modules.deeplink.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mgilangjanuar.dev.goscele.modules.browser.view.BrowserActivity;
import com.mgilangjanuar.dev.goscele.utils.Constant;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class DeepLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        startActivity(new Intent(this, BrowserActivity.class).putExtra(Constant.URL, data.toString()));
        finish();
    }
}
