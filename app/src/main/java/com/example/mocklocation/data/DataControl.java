package com.example.mocklocation.data;

public class DataControl {
    private TempData mTempData = null;

    /**
     * コンストラクタ
     */
    public DataControl() {
        mTempData = new TempData();
    }

    /**
     * TempData を取得
     *
     * @return TempData
     */
    public TempData getTempData() {
        return mTempData;
    }
}
