package com.example.part_timemanager.model;

public class Employee {
    private int mId;
    private String mName;
    private String mAddress;
    private String mPhoneNumber;
    private byte[] mImage_1;
    private byte[] mImage_2;
    //private String mAvatar;


    public Employee() {
    }

    public Employee(String mName, String mAddress, String mPhoneNumber, byte[] mImage_1, byte[] mImage_2) {
        this.mName = mName;
        this.mAddress = mAddress;
        this.mPhoneNumber = mPhoneNumber;
        this.mImage_1 = mImage_1;
        this.mImage_2 = mImage_2;
    }

    public Employee(int mId, String mName, String mAddress, String mPhoneNumber, byte[] mImage_1, byte[] mImage_2) {
        this.mId = mId;
        this.mName = mName;
        this.mAddress = mAddress;
        this.mPhoneNumber = mPhoneNumber;
        this.mImage_1 = mImage_1;
        this.mImage_2 = mImage_2;
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

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public byte[] getmImage_1() {
        return mImage_1;
    }

    public void setmImage_1(byte[] mImage_1) {
        this.mImage_1 = mImage_1;
    }

    public byte[] getmImage_2() {
        return mImage_2;
    }

    public void setmImage_2(byte[] mImage_2) {
        this.mImage_2 = mImage_2;
    }
}
