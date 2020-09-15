package ru.vegd.builder;

import java.time.YearMonth;

public class StreetLevelCrimesLinkBuider {
    private String startLink;
    private Double latitude;
    private Double longitude;
    private YearMonth date;

    private String finalLink;


    public StreetLevelCrimesLinkBuider setStartLink(String startLink) {
        this.startLink = startLink;
        return this;
    }

    public StreetLevelCrimesLinkBuider setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public StreetLevelCrimesLinkBuider setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public StreetLevelCrimesLinkBuider setDate(YearMonth date) {
        this.date = date;
        return this;
    }

    public String build() {
        this.finalLink = new StringBuilder()
                .append(startLink)
                .append("?lng=")
                .append(longitude)
                .append("&lat=")
                .append(latitude)
                .append("&date=")
                .append(date)
                .toString();
        return finalLink;
    }

}
