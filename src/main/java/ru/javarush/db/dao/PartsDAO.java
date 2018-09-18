package ru.javarush.db.dao;

import ru.javarush.db.entity.Part;

import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

public interface PartsDAO
{
    List<Part> deletePart(int id );
    List<Part> addPart(Part part );
    List<Part> updatePart(Part part );
    Part getPart( int id );
    List<Part> getAllParts();
}
