package com.mgilangjanuar.dev.goscele.modules.widget.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.modules.main.view.MainActivity;
import com.mgilangjanuar.dev.goscele.modules.widget.service.ScheduleDailyService;
import com.mgilangjanuar.dev.goscele.utils.Constant;


public class ScheduleDailyWidget extends AppWidgetProvider {

    private static boolean IS_NOT_READY_YET;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_daily_widget);

        Intent intent = new Intent(context, ScheduleDailyService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(appWidgetId, R.id.list_item, intent);

        views.setViewVisibility(R.id.sync_instruction, IS_NOT_READY_YET ? TextView.VISIBLE : TextView.GONE);


        Intent launchMain = new Intent(context, MainActivity.class);
        PendingIntent pendingMainIntent = PendingIntent.getActivity(context, 0, launchMain, 0);
        views.setOnClickPendingIntent(R.id.widget_daily, pendingMainIntent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_item);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        boolean isDataChange = Constant.WIDGET_ACTION_DATA_CHANGE.equals(intent.getAction()) && intent.getBooleanExtra("isEmptyList", false);
        if (IS_NOT_READY_YET != isDataChange) {
            IS_NOT_READY_YET = isDataChange;

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ScheduleDailyWidget.class.getName());
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisAppWidget));
        }
    }
}

