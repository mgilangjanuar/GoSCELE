package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mgilangjanuar.dev.goscele.Adapters.ScheduleAdapter;
import com.mgilangjanuar.dev.goscele.AlarmNotificationCancellation;
import com.mgilangjanuar.dev.goscele.Helpers.ScheduleBroadcastReceiver;
import com.mgilangjanuar.dev.goscele.Models.CalendarEventModel;
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.Models.EventNotificationModel;
import com.mgilangjanuar.dev.goscele.Models.ListScheduleModel;
import com.mgilangjanuar.dev.goscele.Models.ScheduleModel;
import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.Services.CalendarMonthService;
import com.mgilangjanuar.dev.goscele.Services.ScheduleService;

/**
 * Created by muhammadgilangjanuar on 5/17/17.
 */

public class SchedulePresenter {

    Activity activity;
    View view;

    ProgressDialog progress;

    ScheduleService scheduleService;
    CalendarMonthService calendarMonthService;

    ListScheduleModel listScheduleModel;
    CalendarEventModel calendarEventModel;

    public long time;
    public long time2;

    public interface ScheduleServicePresenter {
        void setupSchedule(View view);
    }

    public SchedulePresenter(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        scheduleService = new ScheduleService();
        calendarMonthService = new CalendarMonthService();
        listScheduleModel = new ListScheduleModel(activity);
        calendarEventModel = new CalendarEventModel(activity);

        time = getCurrentTime();
        time2 = getCurrentTime();
    }

    public ScheduleAdapter buildScheduleAdapter() {
        if (listScheduleModel.scheduleModelList.isEmpty() && listScheduleModel.getSavedScheduleModelList() == null) {
            buildListScheduleModel();
        }
        return new ScheduleAdapter(activity, listScheduleModel.scheduleModelList);
    }

    public ScheduleAdapter buildScheduleAdapterForce() {
        clear();
        buildListScheduleModel();
        return new ScheduleAdapter(activity, listScheduleModel.scheduleModelList);
    }

    public void buildCalendarEventModel() {
        try {
            calendarEventModel.clear();
            calendarEventModel.date = calendarMonthService.getMonth(time2);
            calendarEventModel.listEvent = new ArrayList<>();
            for (String e: calendarMonthService.getListDay(time2)) {
                calendarEventModel.listEvent.add(Integer.parseInt(e));
            }
            calendarEventModel.save();
        } catch (IOException e) {
            Log.e("SchedulePresenter", e.getMessage());
        }
    }

    public void buildListScheduleModel() {
        try {
            listScheduleModel.clear();
            listScheduleModel.scheduleModelList = new ArrayList<>();
            List<Map<String, String>> models = scheduleService.getSchedules(time);
            if (models != null) {
                for (Map<String, String> e: models) {
                    ScheduleModel scheduleModel = new ScheduleModel();
                    scheduleModel.title = e.get("title");
                    scheduleModel.date = e.get("date");
                    scheduleModel.url = e.get("url");
                    scheduleModel.description = e.get("description");
                    scheduleModel.time = e.get("time");
                    CourseModel courseModel = new CourseModel();
                    courseModel.name = e.get("course");
                    courseModel.url = e.get("courseUrl");
                    scheduleModel.courseModel = courseModel;
                    listScheduleModel.scheduleModelList.add(scheduleModel);
                }
                listScheduleModel.date = convertTimeToString(time);
            }
            listScheduleModel.save();
        } catch (IOException e) {
            Log.e("SchedulePresenter", e.getMessage());
        }
    }

    public void clear() {
        listScheduleModel.clear();
        calendarEventModel.clear();
    }

    public long getCurrentTime() {
        return convertStringToTime(convertTimeToString(scheduleService.getCurrentTime()));
    }

    public void showProgressDialog() {
        this.showProgressDialog("Fetching data...");
    }

    public void showProgressDialog(String message) {
        progress = new ProgressDialog(activity);
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(true);
        progress.show();
    }

    public void dismissProgressDialog() {
        progress.cancel();
    }

    public String getDate() {
        if (listScheduleModel.getSavedDate() == null) {
            listScheduleModel.date = convertTimeToString(time);
            listScheduleModel.save();
        }
        return listScheduleModel.date;
    }

    public String getDateForce() {
        listScheduleModel.date = convertTimeToString(time);
        listScheduleModel.save();
        return listScheduleModel.date;
    }

    public boolean isEmpty() {
        return listScheduleModel.scheduleModelList.isEmpty();
    }

    public String convertTimeToString(long time) {
        return (new SimpleDateFormat("EEEE, dd MMM yyyy")).format(time * 1000);
    }

    public long convertStringToTime(String date) {
        try {
            return (new SimpleDateFormat("EEEE, dd MMM yyyy")).parse(date).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public CalendarEventModel getCalendarEventModel() {
        return calendarEventModel;
    }

    public ListScheduleModel getListScheduleModel() {
        return listScheduleModel;
    }

    public boolean isCurrentMonth(long time) {
        return (new SimpleDateFormat("MMM yyyy")).format(time * 1000).equals((new SimpleDateFormat("MMM yyyy")).format(time2 * 1000));
    }

    public void notifySchedule() {
        EventNotificationModel model = new EventNotificationModel(activity);
        if (! model.getSavedIsEnableAlarm()) {
            String dayToday = (new SimpleDateFormat("dd")).format(time2 * 1000);
            try {
                for (String e : calendarMonthService.getListDay(time2)) {
                    if (model.getSavedDate() == null || ! model.getSavedDate().equals(e)) {
                        String monthYear = (new SimpleDateFormat("MMM yyyy")).format(time2 * 1000);
                        int eventTime = (int) ((new SimpleDateFormat("dd MMM yyyy")).parse(e + " " + monthYear).getTime() / 1000);
                        if (Integer.parseInt(dayToday) <= Integer.parseInt(e)) {
                            model.date = e;
                            model.isEnableAlarm = true;
                            model.save();
                            List<Map<String, String>> models = scheduleService.getSchedules(eventTime);
                            String title = models.get(0).get("title") + (models.size() > 1 ? " and " + (models.size() - 1) + " events" : "");
                            if (Integer.parseInt(dayToday) == Integer.parseInt(e)) {
                                scheduleNotification(getNotification(title, "You have an event today. Please check your schedule right now!"), 0);
                            } else {
                                int delay = (int) (eventTime - (new Date().getTime() / 1000) + 25200);   // notify at 7 am
                                scheduleNotification(getNotification(title, "You have an event today. Please check your schedule right now!"), delay);
                            }
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("SchedulePresenter", e.getMessage());
            } catch (ParseException e) {
                Log.e("SchedulePresenter", e.getMessage());
            }
        }
    }

    public void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(activity, ScheduleBroadcastReceiver.class);
        notificationIntent.putExtra(ScheduleBroadcastReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(ScheduleBroadcastReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + (delay * 1000);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(activity);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        Intent notificationIntent = new Intent(activity, AlarmNotificationCancellation.class);
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        return builder.build();
    }
}
