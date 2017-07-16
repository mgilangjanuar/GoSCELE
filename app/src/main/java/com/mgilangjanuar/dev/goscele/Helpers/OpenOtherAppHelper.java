package com.mgilangjanuar.dev.goscele.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mjanuar on 7/16/17.
 */

public class OpenOtherAppHelper {

    // source: https://stackoverflow.com/a/23268821
    public static void openLink(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        String packageNameToHide = context.getPackageName();
        ArrayList<Intent> targetIntents = new ArrayList<>();
        for (ResolveInfo currentInfo : activities) {
            String packageName = currentInfo.activityInfo.packageName;
            if (!packageNameToHide.equals(packageName)) {
                Intent targetIntent = new Intent(android.content.Intent.ACTION_VIEW);
                targetIntent.setData(Uri.parse(url));
                targetIntent.setPackage(packageName);
                targetIntents.add(targetIntent);
            }
        }
        Intent chooserIntent = Intent.createChooser(targetIntents.remove(0), "Open with");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
        context.startActivity(chooserIntent);
    }
}
