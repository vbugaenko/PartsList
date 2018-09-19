package ru.javarush.service;

import org.springframework.stereotype.Service;
import ru.javarush.db.dao.PartsDAOImpl;
import ru.javarush.db.entity.Part;

import java.util.List;

/**
 * Services
 *
 * @author Victor Bugaenko
 * @since 18.09.2018
 */
@Service
public class PartsServiceImpl implements PartsService
{
    private PartsDAOImpl partsDAOimpl = new PartsDAOImpl();

    public List<Part> getAllParts()
    {
        return partsDAOimpl.getAllParts();
    }

    @Override
    public void delete(int id)
    {
        partsDAOimpl.deletePart( id );
    }

    @Override
    public void changeEnabledStatus(int id) {
        partsDAOimpl.changeEnabledStatus(id);
    }
}
