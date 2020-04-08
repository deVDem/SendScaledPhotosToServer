package ru.devdem.sendscaledphotososerver.helpers.objects;

import android.graphics.Bitmap;

public class ImageSend {
    private Bitmap mBitmap;
    private Bitmap mPreparedBitmap;
    private int preparedSize;
    private int[] dimens = new int[2];
    private int[] preparedDimens = new int[2];

    public ImageSend() {

    }

    public Bitmap getPreparedBitmap() {
        return mPreparedBitmap;
    }

    public void setPreparedBitmap(Bitmap preparedBitmap) {
        mPreparedBitmap = preparedBitmap;
    }

    public int[] getPreparedDimens() {
        return preparedDimens;
    }

    public void setPreparedDimens(int[] preparedDimens) {
        this.preparedDimens = preparedDimens;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public int getPreparedSize() {
        return preparedSize;
    }

    public void setPreparedSize(int preparedSize) {
        this.preparedSize = preparedSize;
    }

    public int[] getDimens() {
        return dimens;
    }

    public void setDimens(int[] dimens) {
        this.dimens = dimens;
    }

    public void setDimens(int x, int y) {
        this.dimens = new int[]{x, y};
    }
}
