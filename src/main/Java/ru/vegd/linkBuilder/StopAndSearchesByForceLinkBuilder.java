package ru.vegd.linkBuilder;

import ru.vegd.dao.StopAndSearchesByForceDAO;

import java.time.YearMonth;

public class StopAndSearchesByForceLinkBuilder {
    private String startLink;
    private String force;
    private YearMonth date;

    private StopAndSearchesByForceDAO stopAndSearchesByForceDAO;

    private String finalLink;

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

    public StopAndSearchesByForceLinkBuilder setStopAndSearchesDAO(StopAndSearchesByForceDAO stopAndSearchesByForceDAO) {
        this.stopAndSearchesByForceDAO = stopAndSearchesByForceDAO;
        return this;
    }

    public String build() {
        if (stopAndSearchesByForceDAO.checkForAvailability(date, force)[0]) {
            this.finalLink = startLink + "?force=" + force + "&date=" + date;
        } // TODO: throw warn
        // TODO: string builder
        return finalLink;
    }
}
