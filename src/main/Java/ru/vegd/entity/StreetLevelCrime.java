package ru.vegd.entity;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;

public class StreetLevelCrime implements Serializable {

    private static final long serialVersionUID = -1498872298022161553L;

    private String category;

    private String locationType;
    private StreetLocation streetLocation = new StreetLocation();

    private String context;
    private String outcomeCategory;
    private YearMonth outcomeDate;
    private String persistentId;
    private Long id;
    private String locationSubtype;
    private YearMonth month;

    public StreetLevelCrime() {
    }

    public StreetLevelCrime(String category, String locationType, Double latitude, Double longitude, Long streetId, String streetName, String context, String outcomeCategory, YearMonth outcomeDate, String persistentId, Long id, String locationSubtype, YearMonth month) {
        this.category = category;
        this.locationType = locationType;
        this.streetLocation.setLatitude(latitude);
        this.streetLocation.setLongitude(longitude);
        this.streetLocation.setStreetId(streetId);
        this.streetLocation.setStreetName(streetName);
        this.context = context;
        this.outcomeCategory = outcomeCategory;
        this.outcomeDate = outcomeDate;
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
        return locationType;
    }

    public void setLocatonType(String locatonType) {
        this.locationType = locatonType;
    }

    public Double getLatitude() {
        return streetLocation.getLatitude();
    }

    public void setLatitude(Double latitude) {
        streetLocation.setLatitude(latitude);
    }

    public Double getLongitude() {
        return streetLocation.getLongitude();
    }

    public void setLongitude(Double longitude) {
        streetLocation.setLongitude(longitude);
    }

    public Long getStreetId() {
        return streetLocation.getStreetId();
    }

    public void setStreetId(Long streetId) {
        streetLocation.setStreetId(streetId);
    }

    public String getStreetName() {
        return streetLocation.getStreetName();
    }

    public void setStreetName(String streetName) {
        streetLocation.setStreetName(streetName);
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getOutcomeCategory() {
        return outcomeCategory;
    }

    public void setOutcomeCategory(String outcomeCategory) {
        this.outcomeCategory = outcomeCategory;
    }

    public YearMonth getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(YearMonth outcomeDate) {
        this.outcomeDate = outcomeDate;
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
        StreetLevelCrime that = (StreetLevelCrime) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(locationType, that.locationType) &&
                Objects.equals(streetLocation, that.streetLocation) &&
                Objects.equals(context, that.context) &&
                Objects.equals(outcomeCategory, that.outcomeCategory) &&
                Objects.equals(outcomeDate, that.outcomeDate) &&
                Objects.equals(persistentId, that.persistentId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(locationSubtype, that.locationSubtype) &&
                Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, locationType, streetLocation, context, outcomeCategory, outcomeDate, persistentId, id, locationSubtype, month);
    }

    @Override
    public String toString() {
        return "StreetLevelCrime{" +
                "category='" + category + '\'' +
                ", locationType='" + locationType + '\'' +
                ", streetLocation=" + streetLocation +
                ", context='" + context + '\'' +
                ", outcomeCategory='" + outcomeCategory + '\'' +
                ", outcomeDate=" + outcomeDate +
                ", persistentId='" + persistentId + '\'' +
                ", id=" + id +
                ", locationSubtype='" + locationSubtype + '\'' +
                ", month=" + month +
                '}';
    }
}
