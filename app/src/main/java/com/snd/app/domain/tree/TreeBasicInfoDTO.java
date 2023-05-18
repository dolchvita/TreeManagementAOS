package com.snd.app.domain.tree;

import java.time.LocalDate;

public class TreeBasicInfoDTO {
    private String NFC;
    private String species;
    private String submitter;
    private String vendor;
    private LocalDate inserted;
    private LocalDate modified;
    private String hashtag;


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

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
