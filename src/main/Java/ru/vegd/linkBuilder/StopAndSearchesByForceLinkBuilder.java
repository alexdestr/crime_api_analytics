package ru.vegd.linkBuilder;

import java.time.YearMonth;

public class StopAndSearchesByForceLinkBuilder {
    private String startLink;
    private String force;
    private YearMonth date;

    private String finalString;

    public StopAndSearchesByForceLinkBuilder setStartLink(String startLink) {
        this.startLink = startLink;
        return this;
    }

    public StopAndSearchesByForceLinkBuilder setForce(String force) {
        this.force = force;
        return this;
    }

    public StopAndSearchesByForceLinkBuilder setDate(YearMonth date) {
        this.date = date;
        return this;
    }

    public String build() {
        this.finalString = startLink + "?force=" + force + "&date=" + date;
        return finalString;
    }
}
