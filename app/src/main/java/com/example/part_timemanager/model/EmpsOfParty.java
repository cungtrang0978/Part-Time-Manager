package com.example.part_timemanager.model;

public class EmpsOfParty {
    private int mIdEmp;
    private int idParty;

    public EmpsOfParty() {
    }

    public EmpsOfParty(int mIdEmp, int idParty) {
        this.mIdEmp = mIdEmp;
        this.idParty = idParty;
    }

    public int getmIdEmp() {
        return mIdEmp;
    }

    public void setmIdEmp(int mIdEmp) {
        this.mIdEmp = mIdEmp;
    }

    public int getIdParty() {
        return idParty;
    }

    public void setIdParty(int idParty) {
        this.idParty = idParty;
    }
}
