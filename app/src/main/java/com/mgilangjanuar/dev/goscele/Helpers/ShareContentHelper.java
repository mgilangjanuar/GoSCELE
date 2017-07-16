package com.mgilangjanuar.dev.goscele.Helpers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by mjanuar on 7/16/17.
 */

public class ShareContentHelper {
    public static void share(Context context, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(shareIntent, "Share"));
    }
}
