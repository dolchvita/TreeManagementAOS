package com.snd.app.domain.tree;

import java.time.LocalDate;

public class TreeEnvironmentInfoDTO {
    private String NFC;
    private double frameHorizontal;
    private double frameVertical;
    private String frameMaterial;
    private double boundaryStone;
    private double roadWidth;
    private double sidewalkWidth;
    private String packingMaterial;
    private double soilPH;
    private double soilDensity;
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
    public double getFrameHorizontal() {
        return frameHorizontal;
    }
    public void setFrameHorizontal(double frameHorizontal) {
        this.frameHorizontal = frameHorizontal;
    }
    public double getFrameVertical() {
        return frameVertical;
    }
    public void setFrameVertical(double frameVertical) {
        this.frameVertical = frameVertical;
    }
    public String getFrameMaterial() {
        return frameMaterial;
    }
    public void setFrameMaterial(String frameMaterial) {
        this.frameMaterial = frameMaterial;
    }
    public double getBoundaryStone() {
        return boundaryStone;
    }
    public void setBoundaryStone(double boundaryStone) {
        this.boundaryStone = boundaryStone;
    }
    public double getRoadWidth() {
        return roadWidth;
    }
    public void setRoadWidth(double roadWidth) {
        this.roadWidth = roadWidth;
    }
    public double getSidewalkWidth() {
        return sidewalkWidth;
    }
    public void setSidewalkWidth(double sidewalkWidth) {
        this.sidewalkWidth = sidewalkWidth;
    }
    public String getPackingMaterial() {
        return packingMaterial;
    }
    public void setPackingMaterial(String packingMaterial) {
        this.packingMaterial = packingMaterial;
    }
    public double getSoilPH() {
        return soilPH;
    }
    public void setSoilPH(double soilPH) {
        this.soilPH = soilPH;
    }
    public double getSoilDensity() {
        return soilDensity;
    }
    public void setSoilDensity(double soilDensity) {
        this.soilDensity = soilDensity;
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
