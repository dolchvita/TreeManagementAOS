package com.snd.app.domain.tree;

// 수목 위치 상세 정보
public class TreeSpecificLocationInfoDTO {
    private String NFC;
    private Boolean sidewalk;
    private double distance;
    private int carriageway;
    private int orderNumber;


    public String getNFC() {
        return NFC;
    }
    public void setNFC(String NFC) {
        this.NFC = NFC;
    }
    public Boolean getSidewalk() {
        return sidewalk;
    }
    public void setSidewalk(Boolean sidewalk) {
        this.sidewalk = sidewalk;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public int getCarriageway() {
        return carriageway;
    }
    public void setCarriageway(int carriageway) {
        this.carriageway = carriageway;
    }
    public int getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
