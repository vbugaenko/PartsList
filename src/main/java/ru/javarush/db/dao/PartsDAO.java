package ru.javarush.db.dao;

import ru.javarush.db.entity.Part;

import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

public interface PartsDAO
{
    Part getByTitle(String title);
    List<Part> getAllParts();
}
