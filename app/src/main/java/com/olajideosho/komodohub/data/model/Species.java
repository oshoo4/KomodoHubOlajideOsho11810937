package com.olajideosho.komodohub.data.model;

public class Species {
    private int speciesId;
    private String commonName;
    private String scientificName;
    private String description;
    private String conservationStatus;
    private String imageUrl;

    public Species(
            int speciesId,
            String commonName,
            String scientificName,
            String description,
            String conservationStatus,
            String imageUrl
    ) {
        this.speciesId = speciesId;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.description = description;
        this.conservationStatus = conservationStatus;
        this.imageUrl = imageUrl;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getDescription() {
        return description;
    }

    public String getConservationStatus() {
        return conservationStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConservationStatus(String conservationStatus) {
        this.conservationStatus = conservationStatus;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}