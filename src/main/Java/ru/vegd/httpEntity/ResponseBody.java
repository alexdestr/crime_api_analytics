package ru.vegd.httpEntity;

import java.util.ArrayList;
import java.util.List;

public class ResponseBody {
    private List<Response> responseList = new ArrayList<>();

    public List<Response> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<Response> responseList) {
        this.responseList = responseList;
    }

    public void addResponse() {
        responseList.add(new Response());
    }

    public void setResponseRow(Integer id, String row) {
        responseList.get(id).setRow(row);
    }
}

class Response {
    private String row;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }
}
