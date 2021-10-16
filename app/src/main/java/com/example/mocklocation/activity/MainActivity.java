package com.example.mocklocation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mocklocation.R;
import com.example.mocklocation.common.AppCommon;
import com.example.mocklocation.data.TempData;

public class MainActivity extends AppCompatActivity {
    protected static AlarmManager alarmManager;
    private static Button button0;
    private static Button button1;
    private static Button button2;
    private static EditText editTextLat;
    private static EditText editTextLng;
    protected static int timeInterval = 1;
    protected static int howManyTimes = 60;
    private static long endTime;
    private static MockLocationProvider mockNetwork;
    private static MockLocationProvider mockGps;
    protected static PendingIntent pendingIntent;
    private static final int KEEP_GOING = 0;
    protected static Intent serviceIntent;
    private static int SCHEDULE_REQUEST_CODE = 1;
//    private static TempData tempData = AppCommon.getInstance().getAppManager().getDataControl().getTempData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        editTextLat = findViewById(R.id.editText0);
        editTextLng = findViewById(R.id.editText1);


        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                calLatLongFromUI();
                applyLocation();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toast(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.ApplyMockBroadRec_Closed));
        stopMockingLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isFinishing()) {
            toast(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.ApplyMockBroadRec_Closed));
            stopMockingLocation();
        }
    }

    /**
     *
     */
    private static void calLatLongFromUI() {
        TempData tempData = AppCommon.getInstance().getAppManager().getDataControl().getTempData();
        tempData.setmLatitude(Double.parseDouble(editTextLat.getText().toString()));
        tempData.setmLongitude(Double.parseDouble(editTextLng.getText().toString()));
    }

    /**
     * Apply a mocked location, and start an alarm to keep doing it if howManyTimes is > 1
     * This method is called when "Apply" button is pressed.
     */
    private static void applyLocation() {
        TempData tempData = AppCommon.getInstance().getAppManager().getDataControl().getTempData();
        toast(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.MainActivity_MockApplied));

        endTime = System.currentTimeMillis()+ (howManyTimes - 1) * timeInterval * 1000;
        tempData.setEndTime(endTime);

        changeButtonToStop();

        try {
            mockNetwork = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, AppCommon.getInstance().getApplicationContext());
            mockGps = new MockLocationProvider(LocationManager.GPS_PROVIDER, AppCommon.getInstance().getApplicationContext());
        } catch (SecurityException e) {
            e.printStackTrace();
            MainActivity.toast(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.ApplyMockBroadRec_MockNotApplied));
            stopMockingLocation();
            return;
        }

        exec(tempData.getmLatitude(), tempData.getmLongitude());

        if (!hasEnded()) {
            toast(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.MainActivity_MockLocRunning));
            setAlarm(timeInterval);
        } else {
            stopMockingLocation();
        }
    }

    /**
     * Shows a toast
     */
    private static void toast(String str) {
        Toast.makeText(AppCommon.getInstance().getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

    /**
     * Changes the button to Stop, and its behavior.
     */
    private static void changeButtonToStop() {
        button0.setText(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.ActivityMain_Stop));
        button0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stopMockingLocation();
            }

        });
    }

    /**
     * Stops mocking the location.
     */
    protected static void stopMockingLocation() {
        changeButtonToApply();
        TempData tempData = AppCommon.getInstance().getAppManager().getDataControl().getTempData();
        tempData.setEndTime(System.currentTimeMillis() - 1);
        tempData.destroy();

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            toast(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.MainActivity_MockStopped));
        }

        if (mockNetwork != null)
            mockNetwork.shutdown();
        if (mockGps != null)
            mockGps.shutdown();
    }

    /**
     * Changes the button to Apply, and its behavior.
     */
    private static void changeButtonToApply() {
        button0.setText(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.ActivityMain_Apply));
        button0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                calLatLongFromUI();
                applyLocation();
            }

        });
    }

    /**
     * Set a mocked location.
     *
     * @param lat latitude
     * @param lng longitude
     */
    protected static void exec(double lat, double lng) {
        try {
            mockNetwork.pushLocation(lat, lng);
            mockGps.pushLocation(lat, lng);
        } catch (Exception e) {
            toast(AppCommon.getInstance().getApplicationContext().getResources().getString(R.string.MainActivity_MockNotApplied));
            changeButtonToApply();
            e.printStackTrace();
            return;
        }
    }

    /**
     * Check if mocking location should be stopped
     *
     * @return true if it has ended
     */
    protected static boolean hasEnded() {
        TempData tempData = AppCommon.getInstance().getAppManager().getDataControl().getTempData();
        if (howManyTimes == KEEP_GOING) {
            return false;
        } else if (System.currentTimeMillis()> tempData.getEndTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the next alarm accordingly to <seconds>
     *
     * @param seconds number of seconds
     */
    protected static void setAlarm(int seconds) {
        serviceIntent = new Intent(AppCommon.getInstance().getApplicationContext(), ApplyMockBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AppCommon.getInstance().getApplicationContext(), SCHEDULE_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis() + seconds * 1000, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + timeInterval * 1000, pendingIntent);
            }
        } else {
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + timeInterval * 1000, pendingIntent);
        }

//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + seconds * 1000, pendingIntent);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}