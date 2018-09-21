package ru.javarush.service;

import ru.javarush.db.entity.Part;

import java.util.List;

public interface PartsService
{
    List<Part> getAllParts();
    List<Part> searchParts(String pattern);
    void delete(String id);
    void changeEnabledStatus(String id);
    void update(Part part);
    void add(Part part);

    void update(String updateID, String updateTitle, boolean saveEnabled, String updateAmount);
}
