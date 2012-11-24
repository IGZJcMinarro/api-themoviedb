/*
 *      Copyright (c) 2004-2012 Stuart Boston
 *
 *      This software is licensed under a Creative Commons License
 *      See the LICENCE.txt file included in this package
 *
 *      For any reuse or distribution, you must make clear to others the
 *      license terms of this work.
 */
package com.omertron.themoviedbapi.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 *
 * @author Stuart
 */
public class ReleaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * Logger
     */
    private static final Logger logger = Logger.getLogger(ReleaseInfo.class);
    /*
     * Properties
     */
    @JsonProperty("iso_3166_1")
    private String country;
    @JsonProperty("certification")
    private String certification;
    @JsonProperty("release_date")
    private String releaseDate;

    //<editor-fold defaultstate="collapsed" desc="Getter methods">
    public String getCertification() {
        return certification;
    }

    public String getCountry() {
        return country;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter methods">
    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    //</editor-fold>

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown property: '").append(key);
        sb.append("' value: '").append(value).append("'");
        logger.trace(sb.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReleaseInfo other = (ReleaseInfo) obj;
        if ((this.country == null) ? (other.country != null) : !this.country.equals(other.country)) {
            return false;
        }
        if ((this.certification == null) ? (other.certification != null) : !this.certification.equals(other.certification)) {
            return false;
        }
        if ((this.releaseDate == null) ? (other.releaseDate != null) : !this.releaseDate.equals(other.releaseDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.country != null ? this.country.hashCode() : 0);
        hash = 89 * hash + (this.certification != null ? this.certification.hashCode() : 0);
        hash = 89 * hash + (this.releaseDate != null ? this.releaseDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ReleaseInfo=");
        sb.append("[country=").append(country);
        sb.append("],[certification=").append(certification);
        sb.append("],[releaseDate=").append(releaseDate);
        sb.append("]]");
        return sb.toString();
    }
}
