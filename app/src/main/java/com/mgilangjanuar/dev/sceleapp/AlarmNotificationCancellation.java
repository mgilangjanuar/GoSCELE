package com.mgilangjanuar.dev.sceleapp;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mgilangjanuar.dev.sceleapp.Models.EventNotificationModel;

public class AlarmNotificationCancellation extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventNotificationModel model = new EventNotificationModel(this);
        model.date = model.getSavedDate();
        model.isEnableAlarm = false;
        model.save();

        forceRedirect(new Intent(AlarmNotificationCancellation.this, MainActivity.class));
    }
}
