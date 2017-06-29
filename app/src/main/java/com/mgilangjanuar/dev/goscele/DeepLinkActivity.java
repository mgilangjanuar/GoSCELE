package com.mgilangjanuar.dev.goscele;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class DeepLinkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri data = intent.getData();
        forceRedirect(new Intent(DeepLinkActivity.this, SplashScreenActivity.class).putExtra("uri", data));
        onBackPressed();
    }
}
