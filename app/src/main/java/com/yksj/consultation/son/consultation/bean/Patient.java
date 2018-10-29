package com.yksj.consultation.son.consultation.bean;

/**
 * Created by HEKL on 15/10/8.
 */
public class Patient {
    private int PATIENTID;
    private String PATIENTNAME;
    private String PATIENTICON;

    public int getPATIENTID() {
        return PATIENTID;
    }

    public void setPATIENTID(int PATIENTID) {
        this.PATIENTID = PATIENTID;
    }

    public String getPATIENTNAME() {
        return PATIENTNAME;
    }

    public void setPATIENTNAME(String PATIENTNAME) {
        this.PATIENTNAME = PATIENTNAME;
    }

    public String getPATIENTICON() {
        return PATIENTICON;
    }

    public void setPATIENTICON(String PATIENTICON) {
        this.PATIENTICON = PATIENTICON;
    }
}
