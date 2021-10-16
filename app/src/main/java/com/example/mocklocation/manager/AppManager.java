package com.example.mocklocation.manager;

import android.app.Application;
import android.content.Context;

import com.example.mocklocation.data.DataControl;

public class AppManager{
    private DataControl mDataControl = null;

    public AppManager() {
        mDataControl = new DataControl();
    }

    /**
     * DataControl の取得
     *
     * @return DataControl
     */
    public DataControl getDataControl() {
        return mDataControl;
    }
}
