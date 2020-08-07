package ru.vegd.entity;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AvailableStopAndSearchesByForce {
    private YearMonth date;
    private List<String> forcesList = new ArrayList<>();

    public AvailableStopAndSearchesByForce() {}

    public AvailableStopAndSearchesByForce(YearMonth date, List<String> forcesList) {
        this.date = date;
        this.forcesList = forcesList;
    }

    public YearMonth getDate() {
        return date;
    }

    public void setDate(YearMonth date) {
        this.date = date;
    }

    public List<String> getForcesList() {
        return forcesList;
    }

    public void setForcesList(List<String> force) {
        this.forcesList = force;
    }

    public void addForceToList(String force) {
        this.forcesList.add(force);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableStopAndSearchesByForce that = (AvailableStopAndSearchesByForce) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(forcesList, that.forcesList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date, forcesList);
    }

    @Override
    public String toString() {
        return "AvailableStopAndSearchesByForce{" +
                "date=" + date +
                ", forcesList='" + forcesList + '\'' +
                '}';
    }
}
