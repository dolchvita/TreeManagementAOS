package com.snd.app.domain.tree;

import java.time.LocalDate;

public class TreeStatusInfoDTO {
    private String NFC;
    private double DBH;
    private double RCC;
    private double height;
    private double length;
    private double width;
    private boolean pest;
    private LocalDate creation;
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
    public double getDBH() {
        return DBH;
    }
    public void setDBH(double DBH) {
        this.DBH = DBH;
    }
    public double getRCC() {
        return RCC;
    }
    public void setRCC(double RCC) {
        this.RCC = RCC;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public boolean isPest() {
        return pest;
    }
    public void setPest(boolean pest) {
        this.pest = pest;
    }
    public LocalDate getCreation() {
        return creation;
    }
    public void setCreation(LocalDate creation) {
        this.creation = creation;
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