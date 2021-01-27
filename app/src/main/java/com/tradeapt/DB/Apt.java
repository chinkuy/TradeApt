package com.tradeapt.DB;

public class Apt implements Comparable<Apt>{
    private String AptName;
    private String AptPrice;
    private String AptExclusiveUse;
    private String AptFloor;
    private String AptDateMonth;
    private String AptDateDay;


    public Apt() {
        AptName = new String();
        AptPrice = new String();
        AptExclusiveUse = new String();
        AptFloor = new String();
        AptDateMonth = new String();
        AptDateDay = new String();
    }

    public String getAptName() {
        return AptName;
    }

    public String getAptPrice() {
        return AptPrice;
    }

    public String getAptExclusiveUse() {
        return AptExclusiveUse;
    }

    public String getAptFloor() {
        return AptFloor;
    }

    public String getAptDateMonth() {
        return AptDateMonth;
    }

    public String getAptDateDay() {
        return AptDateDay;
    }

    public void setAptName(String aptName) {
        AptName = aptName;
    }

    public void setAptPrice(String aptPrice) {
        AptPrice = aptPrice;
    }

    public void setAptExclusiveUse(String aptExclusiveUse) {
        AptExclusiveUse = aptExclusiveUse;
    }

    public void setAptFloor(String aptFloor) {
        AptFloor = aptFloor;
    }

    public void setAptDateMonth(String aptDateMonth) {
        AptDateMonth = aptDateMonth;
    }

    public void setAptDateDay(String aptDateDay) {
        AptDateDay = aptDateDay;
    }

    @Override
    public int compareTo(Apt apt) {
        return this.AptName.compareTo(apt.getAptName());
    }
}

