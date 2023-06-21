package com.snd.app.domain.tree;


import java.time.LocalDate;

// 수목 위치 정보
public class TreeLocationInfoDTO {
    private String NFC;
    private double latitude;
    private double longitude;
    private String postalCode;
    private String address;
    private String streetCode;
    private String areaCode;
    private int carriageway;
    private boolean sidewalk;
    private int distance;
    private int orderNumber;
    private String submitter;
    private String vendor;
    private LocalDate inserted;
    private LocalDate modified;


    public String getNFC() {
        return NFC;
    }

    public void setNFC(String NFC) {
        this.NFC = NFC;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getCarriageway() {
        return carriageway;
    }

    public void setCarriageway(int carriageway) {
        this.carriageway = carriageway;
    }

    public boolean isSidewalk() {
        return sidewalk;
    }

    public void setSidewalk(boolean sidewalk) {
        this.sidewalk = sidewalk;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getInserted() {
        return inserted;
    }

    public void setInserted(LocalDate inserted) {
        this.inserted = inserted;
    }

    public LocalDate getModified() {
        return modified;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

}