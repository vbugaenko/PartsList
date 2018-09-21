package ru.javarush.db.dao;

import ru.javarush.db.entity.Part;

import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

public interface PartsDAO
{
    void deletePart(int id );
    void addPart(Part part );
    void updatePart(Part part );
    Part getPart( int id );
    List<Part> getAllParts();
    void changeEnabledStatus(int id);
    List<Part> searchParts(String pattern);
}
