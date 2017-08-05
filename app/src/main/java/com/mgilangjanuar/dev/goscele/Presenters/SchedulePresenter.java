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

import com.mgilangjanuar.dev.goscele.Adapters.ScheduleAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.ScheduleDailyAdapter;
import com.mgilangjanuar.dev.goscele.AlarmNotificationCancellationActivity;
import com.mgilangjanuar.dev.goscele.Helpers.ScheduleBroadcastReceiver;
import com.mgilangjanuar.dev.goscele.Models.AccountModel;
import com.mgilangjanuar.dev.goscele.Models.CalendarEventModel;
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.Models.EventNotificationModel;
import com.mgilangjanuar.dev.goscele.Models.ListScheduleModel;
import com.mgilangjanuar.dev.goscele.Models.ScheduleDailyModel;
import com.mgilangjanuar.dev.goscele.Models.ScheduleModel;
import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.Services.AuthSiakService;
import com.mgilangjanuar.dev.goscele.Services.CalendarMonthService;
import com.mgilangjanuar.dev.goscele.Services.ScheduleService;
import com.mgilangjanuar.dev.goscele.Services.ScheduleSiakService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by muhammadgilangjanuar on 5/17/17.
 */

public class SchedulePresenter {

    public long time;
    public long time2;
    private Activity activity;
    private ProgressDialog progress;
    private ScheduleService scheduleService;
    private CalendarMonthService calendarMonthService;
    private ScheduleSiakService scheduleSiakService;
    private AuthSiakService authSiakService;
    private ListScheduleModel listScheduleModel;
    private CalendarEventModel calendarEventModel;
    private ScheduleDailyModel scheduleDailyModel;
    private AccountModel accountModel;

    public SchedulePresenter(Activity activity) {
        this.activity = activity;
        scheduleService = new ScheduleService();
        calendarMonthService = new CalendarMonthService();
        scheduleSiakService = new ScheduleSiakService();
        authSiakService = new AuthSiakService();
        listScheduleModel = new ListScheduleModel(activity);
        calendarEventModel = new CalendarEventModel(activity);
        scheduleDailyModel = new ScheduleDailyModel(activity);
        accountModel = new AccountModel(activity);

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
            if (calendarEventModel.getSavedDate() == null || !calendarEventModel.getSavedDate().equals(calendarMonthService.getMonth(time2))) {
                calendarEventModel.clear();
                calendarEventModel.date = calendarMonthService.getMonth(time2);
                calendarEventModel.listEvent = new ArrayList<>();
                for (String e : calendarMonthService.getListDay(time2)) {
                    calendarEventModel.listEvent.add(Integer.parseInt(e));
                }
                calendarEventModel.save();
            }
        } catch (IOException e) {
            Log.e("SchedulePresenter", String.valueOf(e.getMessage()));
        }
    }

    public void buildListScheduleModel() {
        try {
            listScheduleModel.clear();
            listScheduleModel.scheduleModelList = new ArrayList<>();
            List<Map<String, String>> models = scheduleService.getSchedules(time);
            if (models != null) {
                for (Map<String, String> e : models) {
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
            Log.e("SchedulePresenter", String.valueOf(e.getMessage()));
        }
    }

    public boolean refreshScheduleDaily() {
        scheduleDailyModel.clear();
        if (accountModel.isSaveCredential()) {
            buildScheduleDailyModel(accountModel.getSavedUsername(), accountModel.getSavedPassword());
            return true;
        }
        return false;
    }

    public ScheduleDailyAdapter buildScheduleDailyAdapter() {
        if (scheduleDailyModel.getSavedList() != null && !scheduleDailyModel.getSavedList().isEmpty()) {
            return new ScheduleDailyAdapter(activity, scheduleDailyModel.getSavedList());
        }
        return null;
    }

    public void buildScheduleDailyModel(String username, String password) {
        try {
            authSiakService.login(username, password);
            scheduleDailyModel.clear();
            scheduleDailyModel.list = new ArrayList<>();
            List<List<Map<String, String>>> models = scheduleSiakService.getSchedule();
            if (models != null) {
                int i = 0;
                for (List<Map<String, String>> e1: models) {
                    List<ScheduleDailyModel.Schedule> list = new ArrayList<>();
                    for (Map<String, String> e2: e1) {
                        ScheduleDailyModel.Schedule schedule = new ScheduleDailyModel.Schedule();
                        schedule.time = e2.get("time");
                        schedule.desc = e2.get("desc");
                        list.add(schedule);

                    }
                    ScheduleDailyModel.ListSchedule listSchedule = new ScheduleDailyModel.ListSchedule();
                    listSchedule.day = getDayConstant(i++);
                    listSchedule.scheduleList = list;
                    scheduleDailyModel.list.add(listSchedule);
                }
            }
            scheduleDailyModel.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDayConstant(int i) {
        switch (i) {
            case 0: return "Monday";
            case 1: return "Tuesday";
            case 2: return "Wednesday";
            case 3: return "Thursday";
            case 4: return "Friday";
            case 5: return "Saturday";
            case 6: return "Sunday";
        }
        return null;
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
        if (!model.getSavedIsEnableAlarm()) {
            String dayToday = (new SimpleDateFormat("dd")).format(time2 * 1000);
            try {
                for (String e : calendarMonthService.getListDay(time2)) {
                    if (model.getSavedDate() == null || !model.getSavedDate().equals(e)) {
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
                Log.e("SchedulePresenter", String.valueOf(e.getMessage()));
            } catch (ParseException e) {
                Log.e("SchedulePresenter", String.valueOf(e.getMessage()));
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
        Intent notificationIntent = new Intent(activity, AlarmNotificationCancellationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        return builder.build();
    }

    public interface ScheduleServicePresenter {
        void setupSchedule();
    }
}
