package com.mgilangjanuar.dev.goscele.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by mjanuar on 7/16/17.
 */

public class ShareContentUtil {
    public static void share(Context context, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(shareIntent, "Share"));
    }
}
