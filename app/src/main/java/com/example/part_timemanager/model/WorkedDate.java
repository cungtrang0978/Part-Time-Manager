package com.example.part_timemanager.model;

public class WorkedDate {
    private String mDate;
    private Float mTimeBegin;
    private Float mTimeEnd;
    private Integer salary;

    public WorkedDate(String mDate, Float mTimeBegin, Float mTimeEnd, Integer salary) {
        this.mDate = mDate;
        this.mTimeBegin = mTimeBegin;
        this.mTimeEnd = mTimeEnd;
        this.salary = salary;
    }

    public WorkedDate() {
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
        return mTimeEnd;
    }

    public void setmTimeEnd(Float mTimeEnd) {
        this.mTimeEnd = mTimeEnd;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
