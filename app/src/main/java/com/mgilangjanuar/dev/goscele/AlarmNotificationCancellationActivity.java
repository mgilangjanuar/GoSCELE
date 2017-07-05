package com.mgilangjanuar.dev.goscele;

import android.content.Intent;
import android.os.Bundle;

import com.mgilangjanuar.dev.goscele.Models.EventNotificationModel;

public class AlarmNotificationCancellationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventNotificationModel model = new EventNotificationModel(this);
        model.date = model.getSavedDate();
        model.isEnableAlarm = false;
        model.save();

        forceRedirect(new Intent(AlarmNotificationCancellationActivity.this, MainActivity.class));
    }
}
