package com.example.mocklocation.common;

import android.app.Application;

import com.example.mocklocation.manager.AppManager;

public class AppApplication extends Application {

    /**
     * アプリケーション起動処理
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // アプリケーションコンテキストを保存
        AppCommon.onCreateApplication(getApplicationContext());
        // マネージャのインスタンス作成
        AppCommon.getInstance().setDriveBossManager(new AppManager());
    }
}
