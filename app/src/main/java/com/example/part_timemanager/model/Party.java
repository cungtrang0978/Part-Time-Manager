package com.example.part_timemanager.model;

public class Party {
    private int mId;
    private String mName;
    private String mDate;
    private Float mTimeBegin;
    private Float mTimeEnd;
    private String mFloor;
    private String mLocation;
    private int mHourlyWage;
    private int mNumOfEmps;

    public Party(){}

    public Party(int mId, String mName, String mDate, Float mTimeBegin, Float mTimeEnd, String mFloor, String mLocation, int mHourlyWage, int mNumOfEmps) {
        this.mId = mId;
        this.mName = mName;
        this.mDate = mDate;
        this.mTimeBegin = mTimeBegin;
        this.mTimeEnd = mTimeEnd;
        this.mFloor = mFloor;
        this.mLocation = mLocation;
        this.mHourlyWage = mHourlyWage;
        this.mNumOfEmps = mNumOfEmps;
    }

    public Party(String mName, String mDate, Float mTimeBegin, Float mTimeEnd, String mFloor, String mLocation, int mHourlyWage, int mNumOfEmps) {
        this.mName = mName;
        this.mDate = mDate;
        this.mTimeBegin = mTimeBegin;
        this.mTimeEnd = mTimeEnd;
        this.mFloor = mFloor;
        this.mLocation = mLocation;
        this.mHourlyWage = mHourlyWage;
        this.mNumOfEmps = mNumOfEmps;
    }

    public Party(String mName, String mDate, Float mTimeBegin, String mFloor, String mLocation, int mHourlyWage, int mNumOfEmps) {
        this.mName = mName;
        this.mDate = mDate;
        this.mTimeBegin = mTimeBegin;
        this.mFloor = mFloor;
        this.mLocation = mLocation;
        this.mHourlyWage = mHourlyWage;
        this.mNumOfEmps = mNumOfEmps;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public Float getmTimeBegin() {
        return mTimeBegin;
    }

    public void setmTimeBegin(Float mTimeBegin) {
        this.mTimeBegin = mTimeBegin;
    }

    public Float getmTimeEnd() {
        if(mTimeEnd==null){
            return null;
        }
        return mTimeEnd;
    }

    public void setmTimeEnd(Float mTimeEnd) {
        if(mTimeEnd==null){
            this.mTimeEnd =null;
        }
        this.mTimeEnd = mTimeEnd;
    }

    public String getmFloor() {
        return mFloor;
    }

    public void setmFloor(String mFloor) {
        this.mFloor = mFloor;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public Integer getmHourlyWage() {
        return mHourlyWage;
    }

    public void setmHourlyWage(int mHourlyWage) {
        this.mHourlyWage = mHourlyWage;
    }

    public Integer getmNumOfEmps() {
        return mNumOfEmps;
    }

    public void setmNumOfEmps(int mNumOfEmps) {
        this.mNumOfEmps = mNumOfEmps;
    }
}
