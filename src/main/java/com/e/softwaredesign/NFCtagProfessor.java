package com.e.softwaredesign;

public class NFCtagProfessor {
    private String professorName;
    private String str_className;
    private int startTime,endTime;
    private String NFCkey;
    private String str_Day;

    public String getStr_className() {
        return str_className;
    }

    public void setStr_className(String str_className) {
        this.str_className = str_className;
    }

    public String getStr_Day() {
        return str_Day;
    }

    public void setStr_Day(String str_Day) {
        this.str_Day = str_Day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getNFCkey() {
        return NFCkey;
    }

    public void setNFCkey(String NFCkey) {
        this.NFCkey = NFCkey;
    }
}
