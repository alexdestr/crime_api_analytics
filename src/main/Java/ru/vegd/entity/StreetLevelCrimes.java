package ru.vegd.entity;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

public class StreetLevelCrimes {
    private String category;

    private String locatonType;
    private Double latitude;
    private Double longitude;
    private Long streetId;
    private String streetName;

    private Long context;
    private String outcomeCategory;
    private String persistentId;
    private Long id;
    private String locationSubtype;
    private YearMonth month;

    public StreetLevelCrimes(String category, String locatonType, List<Double> location, Long streetId, String streetName, Long context, String outcomeCategory, String persistentId, Long id, String locationSubtype, YearMonth month) {
        this.category = category;
        this.locatonType = locatonType;
        this.latitude = location.get(0);
        this.longitude = location.get(1);
        this.streetId = streetId;
        this.streetName = streetName;
        this.context = context;
        this.outcomeCategory = outcomeCategory;
        this.persistentId = persistentId;
        this.id = id;
        this.locationSubtype = locationSubtype;
        this.month = month;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocatonType() {
        return locatonType;
    }

    public void setLocatonType(String locatonType) {
        this.locatonType = locatonType;
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

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Long getContext() {
        return context;
    }

    public void setContext(Long context) {
        this.context = context;
    }

    public String getOutcomeCategory() {
        return outcomeCategory;
    }

    public void setOutcomeCategory(String outcomeCategory) {
        this.outcomeCategory = outcomeCategory;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationSubtype() {
        return locationSubtype;
    }

    public void setLocationSubtype(String locationSubtype) {
        this.locationSubtype = locationSubtype;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetLevelCrimes that = (StreetLevelCrimes) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(locatonType, that.locatonType) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(streetId, that.streetId) &&
                Objects.equals(streetName, that.streetName) &&
                Objects.equals(context, that.context) &&
                Objects.equals(outcomeCategory, that.outcomeCategory) &&
                Objects.equals(persistentId, that.persistentId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(locationSubtype, that.locationSubtype) &&
                Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, locatonType, latitude, longitude, streetId, streetName, context, outcomeCategory, persistentId, id, locationSubtype, month);
    }

    @Override
    public String toString() {
        return "StreetLevelCrimes{" +
                "category='" + category + '\'' +
                ", locatonType='" + locatonType + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", streetId=" + streetId +
                ", streetName='" + streetName + '\'' +
                ", context=" + context +
                ", outcomeCategory='" + outcomeCategory + '\'' +
                ", persistentId='" + persistentId + '\'' +
                ", id=" + id +
                ", locationSubtype='" + locationSubtype + '\'' +
                ", month=" + month +
                '}';
    }
}
