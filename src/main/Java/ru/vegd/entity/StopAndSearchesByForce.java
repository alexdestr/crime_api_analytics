package ru.vegd.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class StopAndSearchesByForce implements Serializable {

    private static final long serialVersionUID = -796366176847932127L;

    private String type;
    private String involvedPerson;
    private Timestamp dateTime;
    private Boolean operation;
    private String operationName;
    private StreetLocation streetLocation = new StreetLocation();
    private Gender gender;
    private String ageRange;
    private String selfDefinedEthnicity;
    private String officerDefinedEthnicity;
    private String legislation;
    private String objectOfSearch;
    private String outcome;
    private Boolean outcomeLinkedToObjectOfSearch;
    private Boolean removalOfMoreThanOuterClothing;

    public StopAndSearchesByForce() {
    }

    public StopAndSearchesByForce(String type, String involvedPerson, Timestamp dateTime, Boolean operation, String operationName, Double latitude, Double longitude, Long streetId, String streetName, Gender gender, String ageRange, String selfDefinedEthnicity, String officerDefinedEthnicity, String legislation, String objectOfSearch, String outcome, Boolean outcomeLinkedToObjectOfSearch, Boolean removalOfMoreThanOuterClothing) {
        this.type = type;
        this.involvedPerson = involvedPerson;
        this.dateTime = dateTime;
        this.operation = operation;
        this.operationName = operationName;
        this.streetLocation.setLatitude(latitude);
        this.streetLocation.setLongitude(longitude);
        this.streetLocation.setStreetId(streetId);
        this.streetLocation.setStreetName(streetName);
        this.gender = gender;
        this.ageRange = ageRange;
        this.selfDefinedEthnicity = selfDefinedEthnicity;
        this.officerDefinedEthnicity = officerDefinedEthnicity;
        this.legislation = legislation;
        this.objectOfSearch = objectOfSearch;
        this.outcome = outcome;
        this.outcomeLinkedToObjectOfSearch = outcomeLinkedToObjectOfSearch;
        this.removalOfMoreThanOuterClothing = removalOfMoreThanOuterClothing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvolvedPerson() {
        return involvedPerson;
    }

    public void setInvolvedPerson(String involvedPerson) {
        this.involvedPerson = involvedPerson;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getOperation() {
        return operation;
    }

    public void setOperation(Boolean operation) {
        this.operation = operation;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getSelfDefinedEthnicity() {
        return selfDefinedEthnicity;
    }

    public void setSelfDefinedEthnicity(String selfDefinedEthnicity) {
        this.selfDefinedEthnicity = selfDefinedEthnicity;
    }

    public String getOfficerDefinedEthnicity() {
        return officerDefinedEthnicity;
    }

    public void setOfficerDefinedEthnicity(String officerDefinedEthnicity) {
        this.officerDefinedEthnicity = officerDefinedEthnicity;
    }

    public String getLegislation() {
        return legislation;
    }

    public void setLegislation(String legislation) {
        this.legislation = legislation;
    }

    public String getObjectOfSearch() {
        return objectOfSearch;
    }

    public void setObjectOfSearch(String objectOfSearch) {
        this.objectOfSearch = objectOfSearch;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Boolean getOutcomeLinkedToObjectOfSearch() {
        return outcomeLinkedToObjectOfSearch;
    }

    public void setOutcomeLinkedToObjectOfSearch(Boolean outcomeLinkedToObjectOfSearch) {
        this.outcomeLinkedToObjectOfSearch = outcomeLinkedToObjectOfSearch;
    }

    public Boolean getRemovalOfMoreThanOuterClothing() {
        return removalOfMoreThanOuterClothing;
    }

    public void setRemovalOfMoreThanOuterClothing(Boolean removalOfMoreThanOuterClothing) {
        this.removalOfMoreThanOuterClothing = removalOfMoreThanOuterClothing;
    }

    @Override
    public String toString() {
        return "StopAndSearchesByForce{" +
                "type='" + type + '\'' +
                ", involvedPerson='" + involvedPerson + '\'' +
                ", dateTime=" + dateTime +
                ", operation=" + operation +
                ", operationName='" + operationName + '\'' +
                ", streetLocation=" + streetLocation +
                ", gender=" + gender +
                ", ageRange='" + ageRange + '\'' +
                ", selfDefinedEthnicity='" + selfDefinedEthnicity + '\'' +
                ", officerDefinedEthnicity='" + officerDefinedEthnicity + '\'' +
                ", legislation='" + legislation + '\'' +
                ", objectOfSearch='" + objectOfSearch + '\'' +
                ", outcome='" + outcome + '\'' +
                ", outcomeLinkedToObjectOfSearch=" + outcomeLinkedToObjectOfSearch +
                ", removalOfMoreThanOuterClothing=" + removalOfMoreThanOuterClothing +
                '}';
    }
}
