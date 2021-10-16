package com.example.mocklocation.data;

public class TempData {
    private static final String TAG = TempData.class.getSimpleName();

    //
    private long endTime = 0L;
    private long calSysTime = 0L;
    //位置情報
    private double mLatitude = 0; //現在の緯度
    private double mLongitude = 0; //現在の経度
    //

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setCalSysTime(long calSysTime) {
        this.calSysTime = calSysTime;
    }

    public long getCalSysTime() {
        return calSysTime;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    /**
     * コンストラクタ
     */
    public TempData() {
        endTime = 0L;
        mLatitude = 0; //現在の緯度
        mLongitude = 0; //現在の経度
    }

    /**
     * 値をクリア
     */
    public void destroy() {
        endTime = 0L;
        mLatitude = 0; //現在の緯度
        mLongitude = 0; //現在の経度
    }
}
