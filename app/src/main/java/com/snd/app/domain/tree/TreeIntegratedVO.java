package com.snd.app.domain.tree;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 수목의 모든 정보를 담는 객체
public class TreeIntegratedVO {
   private static TreeIntegratedVO instance;

    public static TreeIntegratedVO getInstance() {
        if(instance==null){
            instance=new TreeIntegratedVO();
        }
        return instance;
    }

    private TreeIntegratedVO(){
        // 생성자 막기
    }

    /*Data Of TreeBasicInfo*/
    private String NFC;
    private String species;
    private String basicSubmitter;
    private String basicVendor;
    private LocalDateTime basicInserted;
    private LocalDateTime basicModified;
    private String hashtag;

    /*Data Of TreeLocationInfo*/
    private Double latitude;
    private Double longitude;
    private String postalCode;
    private String address;
    private String streetCode;
    private String areaCode;

    private Integer carriageway;
    private Boolean sidewalk;
    private Double distance;

    private Integer orderNumber;
    private String locationSubmitter;
    private String locationVendor;
    private LocalDateTime locationInserted;
    private LocalDateTime locationModified;

    /*Data Of TreeStatusInfo*/
    private Double DBH;         // 흉고직경
    private Double RCC;         // 근원직경
    private Double height;      // 수고
    private Double length;      // 지하고
    private Double width;       // 수관폭
    private Boolean pest;       // 병충해 유무
    private LocalDate creation;
    private String statusSubmitter;
    private String statusVendor;
    private LocalDateTime statusInserted;
    private LocalDateTime statusModified;

    /*Data Of TreeEnvironmentInfo*/
    private Double frameHorizontal;
    private Double frameVertical;
    private String frameMaterial;
    private Double boundaryStone;
    private Double roadWidth;
    private Double sideWalkWidth;
    private String packingMaterial;
    private Double soilPH;
    private Double soilDensity;
    private String environmentSubmitter;
    private String environmentVendor;
    private LocalDateTime environmentInserted;
    private LocalDateTime environmentModified;



    public String getNFC() {
        return NFC;
    }

    public void setNFC(String NFC) {
        this.NFC = NFC;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBasicSubmitter() {
        return basicSubmitter;
    }

    public void setBasicSubmitter(String basicSubmitter) {
        this.basicSubmitter = basicSubmitter;
    }

    public String getBasicVendor() {
        return basicVendor;
    }

    public void setBasicVendor(String basicVendor) {
        this.basicVendor = basicVendor;
    }

    public LocalDateTime getBasicInserted() {
        return basicInserted;
    }

    public void setBasicInserted(LocalDateTime basicInserted) {
        this.basicInserted = basicInserted;
    }

    public LocalDateTime getBasicModified() {
        return basicModified;
    }

    public void setBasicModified(LocalDateTime basicModified) {
        this.basicModified = basicModified;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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

    public Integer getCarriageway() {
        return carriageway;
    }

    public void setCarriageway(Integer carriageway) {
        this.carriageway = carriageway;
    }

    public Boolean getSidewalk() {
        return sidewalk;
    }

    public void setSidewalk(Boolean sidewalk) {
        this.sidewalk = sidewalk;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLocationSubmitter() {
        return locationSubmitter;
    }

    public void setLocationSubmitter(String locationSubmitter) {
        this.locationSubmitter = locationSubmitter;
    }

    public String getLocationVendor() {
        return locationVendor;
    }

    public void setLocationVendor(String locationVendor) {
        this.locationVendor = locationVendor;
    }

    public LocalDateTime getLocationInserted() {
        return locationInserted;
    }

    public void setLocationInserted(LocalDateTime locationInserted) {
        this.locationInserted = locationInserted;
    }

    public LocalDateTime getLocationModified() {
        return locationModified;
    }

    public void setLocationModified(LocalDateTime locationModified) {
        this.locationModified = locationModified;
    }

    public Double getDBH() {
        return DBH;
    }

    public void setDBH(Double DBH) {
        this.DBH = DBH;
    }

    public Double getRCC() {
        return RCC;
    }

    public void setRCC(Double RCC) {
        this.RCC = RCC;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Boolean getPest() {
        return pest;
    }

    public void setPest(Boolean pest) {
        this.pest = pest;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }

    public String getStatusSubmitter() {
        return statusSubmitter;
    }

    public void setStatusSubmitter(String statusSubmitter) {
        this.statusSubmitter = statusSubmitter;
    }

    public String getStatusVendor() {
        return statusVendor;
    }

    public void setStatusVendor(String statusVendor) {
        this.statusVendor = statusVendor;
    }

    public LocalDateTime getStatusInserted() {
        return statusInserted;
    }

    public void setStatusInserted(LocalDateTime statusInserted) {
        this.statusInserted = statusInserted;
    }

    public LocalDateTime getStatusModified() {
        return statusModified;
    }

    public void setStatusModified(LocalDateTime statusModified) {
        this.statusModified = statusModified;
    }

    public Double getFrameHorizontal() {
        return frameHorizontal;
    }

    public void setFrameHorizontal(Double frameHorizontal) {
        this.frameHorizontal = frameHorizontal;
    }

    public Double getFrameVertical() {
        return frameVertical;
    }

    public void setFrameVertical(Double frameVertical) {
        this.frameVertical = frameVertical;
    }

    public String getFrameMaterial() {
        return frameMaterial;
    }

    public void setFrameMaterial(String frameMaterial) {
        this.frameMaterial = frameMaterial;
    }

    public Double getBoundaryStone() {
        return boundaryStone;
    }

    public void setBoundaryStone(Double boundaryStone) {
        this.boundaryStone = boundaryStone;
    }

    public Double getRoadWidth() {
        return roadWidth;
    }

    public void setRoadWidth(Double roadWidth) {
        this.roadWidth = roadWidth;
    }

    public Double getSideWalkWidth() {
        return sideWalkWidth;
    }

    public void setSideWalkWidth(Double sideWalkWidth) {
        this.sideWalkWidth = sideWalkWidth;
    }

    public String getPackingMaterial() {
        return packingMaterial;
    }

    public void setPackingMaterial(String packingMaterial) {
        this.packingMaterial = packingMaterial;
    }

    public Double getSoilPH() {
        return soilPH;
    }

    public void setSoilPH(Double soilPH) {
        this.soilPH = soilPH;
    }

    public Double getSoilDensity() {
        return soilDensity;
    }

    public void setSoilDensity(Double soilDensity) {
        this.soilDensity = soilDensity;
    }

    public String getEnvironmentSubmitter() {
        return environmentSubmitter;
    }

    public void setEnvironmentSubmitter(String environmentSubmitter) {
        this.environmentSubmitter = environmentSubmitter;
    }

    public String getEnvironmentVendor() {
        return environmentVendor;
    }

    public void setEnvironmentVendor(String environmentVendor) {
        this.environmentVendor = environmentVendor;
    }

    public LocalDateTime getEnvironmentInserted() {
        return environmentInserted;
    }

    public void setEnvironmentInserted(LocalDateTime environmentInserted) {
        this.environmentInserted = environmentInserted;
    }

    public LocalDateTime getEnvironmentModified() {
        return environmentModified;
    }

    public void setEnvironmentModified(LocalDateTime environmentModified) {
        this.environmentModified = environmentModified;
    }
}
