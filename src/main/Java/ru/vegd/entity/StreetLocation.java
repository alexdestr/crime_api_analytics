package ru.vegd.entity;

import java.util.Objects;

public class StreetLocation {
    private String streetName;
    private Double latitude;
    private Double longitude;

    public StreetLocation(String streetName, Double latitude, Double longitude) {
        this.streetName = streetName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {

        return Objects.hash(streetName, latitude, longitude);
    }


}
