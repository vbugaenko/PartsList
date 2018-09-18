package ru.javarush.service;

import ru.javarush.db.dao.PartsDAOImpl;
import ru.javarush.db.entity.Part;

import java.util.List;

/**
 * Services
 *
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

public class PartsService
{
    private PartsDAOImpl partsDAOimpl = new PartsDAOImpl();

    public List<Part> getAllParts()
    {
        return partsDAOimpl.getAllParts();
    }
}
