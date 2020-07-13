package ru.vegd.entity;

import java.io.Serializable;
import java.util.Objects;

public class StreetLocation implements Serializable {

    private static final long serialVersionUID = 4117802432774264239L;

    private String streetName;
    private Long streetId;
    private Double latitude;
    private Double longitude;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetLocation that = (StreetLocation) o;
        return Objects.equals(streetName, that.streetName) &&
                Objects.equals(streetId, that.streetId) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {

        return Objects.hash(streetName, streetId, latitude, longitude);
    }

    @Override
    public String toString() {
        return "StreetLocation{" +
                "streetName='" + streetName + '\'' +
                ", streetId=" + streetId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
