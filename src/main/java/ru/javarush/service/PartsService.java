package ru.javarush.service;

import ru.javarush.db.entity.Part;

import java.util.List;

public interface PartsService
{
    List<Part> getParts(String filter, String search, String page);
    void delete(String id);
    void changeEnabledStatus(String id);
    void update(Part part);
    void add(Part part);
    void add(String addTitle, String addEnabled, String addAmount);
    Enum filerEnum(String filter);
    void update(String updateID, String updateTitle, boolean saveEnabled, String updateAmount);
    int min();
    int getPagesCalc();
}
