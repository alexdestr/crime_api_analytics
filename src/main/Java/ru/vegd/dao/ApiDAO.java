package ru.vegd.dao;

import ru.vegd.entity.RequestBody;

import java.util.List;

public interface ApiDAO {
    RequestBody getDataByRequest(RequestBody requestBody);
}
