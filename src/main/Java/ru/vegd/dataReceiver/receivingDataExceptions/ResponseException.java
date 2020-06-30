package ru.vegd.dataReceiver.receivingDataExceptions;

import java.util.Objects;

public class ResponseException extends Exception {

    private Integer errorNumber;
    private String errorMsg;

    public ResponseException(String errorMsg, Integer errorNumber) {
        super(errorMsg + errorNumber);
        this.errorMsg = errorMsg;
        this.errorNumber = errorNumber;
    }

    public Integer getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseException that = (ResponseException) o;
        return Objects.equals(errorNumber, that.errorNumber) &&
                Objects.equals(errorMsg, that.errorMsg);
    }

    @Override
    public int hashCode() {

        return Objects.hash(errorNumber, errorMsg);
    }

    @Override
    public String toString() {
        return "ResponseException{" +
                "errorNumber=" + errorNumber +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
