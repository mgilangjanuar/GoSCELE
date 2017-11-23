package com.mgilangjanuar.dev.goscele.modules.widget.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyGroupModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyModel;
import com.mgilangjanuar.dev.goscele.modules.widget.view.ScheduleDailyWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ScheduleDailyProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<ScheduleDailyGroupModel> list;

    public ScheduleDailyProvider(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    private void populateData(){
        list = new ScheduleDailyGroupModel().find().execute();

//        if (list.isEmpty()) {
//            Intent intent = new Intent(context, ScheduleDailyWidget.class);
//            intent.setAction("CHECK_LIST");
//            intent.putExtra("isEmptyList", list.isEmpty());
//            context.sendBroadcast(intent);
//        }
    }


    @Override
    public void onCreate() {
        populateData();
    }

    @Override
    public void onDataSetChanged() {
        // populateData();
        Intent intent = new Intent(context, ScheduleDailyWidget.class);
        intent.setAction("CHECK_LIST");
        intent.putExtra("isEmptyList", list.isEmpty());
        context.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        ScheduleDailyGroupModel model = list.get(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_schedule_daily_widget);

        remoteViews.setTextViewText(R.id.title_schedule_daily, model.day);

        CharSequence builder = null;
        for (ScheduleDailyModel e : model.scheduleDailyModels()) {
            String course = e.desc.substring(0, e.desc.indexOf(" Ruang: "));
            String room = e.desc.substring(e.desc.indexOf(" Ruang: ") + 8);
            SpannableString title = new SpannableString(course);
            title.setSpan(new RelativeSizeSpan(1.2f), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString time = new SpannableString("R. " + room + "\t\t" + e.time);
            time.setSpan(new ForegroundColorSpan(context.getResources().getColor(android.R.color.darker_gray)), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (builder == null) {
                builder = TextUtils.concat(title, "\n", time + "\n\n");
            } else {
                builder = TextUtils.concat(builder, title, "\n", time + "\n\n");
            }
        }
        remoteViews.setTextViewText(R.id.content_schedule_daily, builder == null ? "(empty)\n\n" : builder);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
