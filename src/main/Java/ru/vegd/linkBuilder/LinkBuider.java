package ru.vegd.linkBuilder;

import java.time.YearMonth;

public class LinkBuider {
    private String startLink;
    private Double latitude;
    private Double longitude;
    private YearMonth date;

    private String finalLink;


    public LinkBuider setStartLink(String startLink) {
        this.startLink = startLink;
        return this;
    }

    public LinkBuider setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LinkBuider setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public LinkBuider setDate(YearMonth date) {
        this.date = date;
        return this;
    }

    public String build() {
        this.finalLink = startLink + "?lng=" + longitude + "&lat=" + latitude + "&date=" + date;
        return finalLink;
    }

}
