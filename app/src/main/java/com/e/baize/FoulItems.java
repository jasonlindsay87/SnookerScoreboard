package com.e.baize;

public class FoulItems {
    private float mFoulPoints;
    private int mFoulImage;

    public FoulItems(float foulPoints, Integer foulImage){
        mFoulPoints = foulPoints;
        if (foulImage != null) {
            mFoulImage = foulImage;
        }
    }
    public float getFoulPoints() {
        return mFoulPoints;
    }
    public int getFoulImage() {
        return mFoulImage;
    }

}
