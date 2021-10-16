package com.example.mocklocation.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mocklocation.activity.MainActivity;
import com.example.mocklocation.common.AppCommon;
import com.example.mocklocation.data.TempData;

public class ApplyMockBroadcastReceiver extends BroadcastReceiver {
    Intent serviceIntent;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    TempData tempData = AppCommon.getInstance().getAppManager().getDataControl().getTempData();

    public ApplyMockBroadcastReceiver() {
        alarmManager = MainActivity.alarmManager;
        serviceIntent = MainActivity.serviceIntent;
        pendingIntent = MainActivity.pendingIntent;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            MainActivity.exec(tempData.getmLatitude(), tempData.getmLongitude());

            if (!MainActivity.hasEnded()) {
                MainActivity.setAlarm(MainActivity.timeInterval);
            } else {
                MainActivity.stopMockingLocation();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
