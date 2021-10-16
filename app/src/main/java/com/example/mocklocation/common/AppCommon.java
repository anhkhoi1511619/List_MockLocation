package com.example.mocklocation.common;

import android.content.Context;

import com.example.mocklocation.manager.AppManager;

public class AppCommon {
    private static AppCommon sInstance = null;
    private Context mApplicationContext = null;
    private AppManager mAppManager = null;

    static void onCreateApplication(Context applicationContext) {
        // Application#onCreateのタイミングでシングルトンが生成される
        sInstance = new AppCommon(applicationContext);
    }

    /**
     * コンストラクタ
     *
     * @param applicationContext アプリケーションコンテキスト
     */
    private AppCommon(Context applicationContext) {
        this.mApplicationContext = applicationContext;
    }

    /**
     * インスタンスの取得
     *
     * @return DriveBossCommon インスタンス
     */
    public static AppCommon getInstance() {
        return sInstance;
    }

    /**
     * DRIVEBOSS マネージャの登録
     */
    // publicをつけないのは意図的
    // MyApplicationと同じパッケージにして、このメソッドのアクセスレベルはパッケージローカルとする
    // 念のため意図しないところで呼び出されることを防ぐため
    void setDriveBossManager(AppManager appManager) {
        this.mAppManager = appManager;
    }
    /**
     * アプリケーションコンテキストの取得
     *
     * @return Context アプリケーションコンテキスト
     */
    public Context getApplicationContext() {
        return mApplicationContext;
    }

    /**
     * DRIVEBOSS マネージャの取得
     *
     * @return DriveBossManager
     */
    public AppManager getAppManager() {
        return mAppManager;
    }
}
