package ru.vegd.dao;

import ru.vegd.http.RequestBody;
import ru.vegd.http.ResponseBody;

import java.util.List;

public interface ApiDAO {
    List<ResponseBody> getDataByRequest(RequestBody requestBody);
}
