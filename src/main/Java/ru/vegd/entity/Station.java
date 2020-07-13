package ru.vegd.entity;

import java.io.Serializable;
import java.util.Objects;

public class Station implements Serializable {

    private static final long serialVersionUID = -9180161040011813304L;

    private String streetName;
    private Double latitude;
    private Double longitude;

    public Station(String streetName, Double longitude, Double latitude) {
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
        Station station = (Station) o;
        return Objects.equals(streetName, station.streetName) &&
                Objects.equals(latitude, station.latitude) &&
                Objects.equals(longitude, station.longitude);
    }

    @Override
    public int hashCode() {

        return Objects.hash(streetName, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Station{" +
                "streetName='" + streetName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
