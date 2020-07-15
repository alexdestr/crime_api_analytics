package ru.vegd.dao;

import ru.vegd.entity.Force;

import java.util.List;

public interface ForcesListDAO {

    void add(List<Force> force);
    List<Force> getAllForces();

}
