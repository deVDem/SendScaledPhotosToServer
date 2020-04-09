package ru.devdem.sendscaledphotososerver.helpers.objects;

import android.graphics.Bitmap;

public class ImageSend {
    private String mPreparedBase64;
    private Bitmap mPreviewBitmap;
    private int preparedSize;
    private int[] dimens = new int[2];
    private int[] preparedDimens = new int[2];

    public ImageSend() {

    }

    public Bitmap getPreviewBitmap() {
        return mPreviewBitmap;
    }

    public void setPreviewBitmap(Bitmap previewBitmap) {
        mPreviewBitmap = previewBitmap;
    }

    public String getPreparedBase64() {
        return mPreparedBase64;
    }

    public void setPreparedBase64(String preparedBase64) {
        mPreparedBase64 = preparedBase64;
    }

    public int[] getPreparedDimens() {
        return preparedDimens;
    }

    public void setPreparedDimens(int[] preparedDimens) {
        this.preparedDimens = preparedDimens;
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
