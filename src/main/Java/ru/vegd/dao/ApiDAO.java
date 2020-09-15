package ru.vegd.dao;

import ru.vegd.httpEntity.RequestBody;
import ru.vegd.httpEntity.ResponseBody;

import java.util.List;

public interface ApiDAO {
    List<ResponseBody> getDataByRequest(RequestBody requestBody);
}
