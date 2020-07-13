package ru.vegd.dao;

import ru.vegd.entity.CrimeCategory;

import java.util.List;

public interface CrimeCategoriesDao {

    void add(List<CrimeCategory> categoryList);

}
