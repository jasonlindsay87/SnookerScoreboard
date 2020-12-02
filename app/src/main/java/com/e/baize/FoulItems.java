package com.e.baize;

public class FoulItems {
    private int mFoulPoints;
    private int mFoulImage;

    public FoulItems(int foulPoints, Integer foulImage){
        mFoulPoints = foulPoints;
        if (foulImage != null) {
            mFoulImage = foulImage;
        }
    }
    public int getFoulPoints() {
        return mFoulPoints;
    }
    public int getFoulImage() {
        return mFoulImage;
    }

}
