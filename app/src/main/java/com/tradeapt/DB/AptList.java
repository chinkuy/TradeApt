package com.tradeapt.DB;

public class AptList{
    private String AptName;
    private String AptPrice;
    private String AptExclusiveUse;
    private String AptFloor;
    private String AptDateMonth;
    private String AptDateDay;


    public AptList() {
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

        if(aptName.equals("e-편한세상")){
            aptName = "e편한세상";
        }
        if(aptName.equals("살구골마을(진덕,서광,성지,동아)")){
            aptName = "살구골마을";
        }
        if(aptName.equals("황골마을(쌍용)")){
            aptName = "황골마을";
        }

        if(aptName.equals("래미안 영통마크원 2단지")){
            aptName = "래미안영통마크원2단지";
        }

        if(aptName.equals("래미안 영통마크원 1단지")){
            aptName = "래미안영통마크원1단지";
        }

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
}

