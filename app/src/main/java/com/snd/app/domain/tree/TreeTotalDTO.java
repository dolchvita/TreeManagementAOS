package com.snd.app.domain.tree;


// 기존 나무들의 정보들을 통합해서 앱에서만 사용할 객체
public class TreeTotalDTO {
    private String NFC;
    private double latitude;
    private double longitude;
    private String species;


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
    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }


}
