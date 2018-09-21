package ru.javarush.service;

import org.springframework.stereotype.Service;
import ru.javarush.db.dao.PartsDAOImpl;
import ru.javarush.db.entity.Part;
import ru.javarush.service.utility.IntFromStringImpl;

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
    private IntFromStringImpl intFromString = new IntFromStringImpl();

    public List<Part> getAllParts()
    {
        return partsDAOimpl.getAllParts();
    }

    /**
     * Поиск запчастей в базе осуществляется вне зависимости от регистра.
     */
    @Override
    public List<Part> searchParts(String pattern)
    {
        return partsDAOimpl.searchParts(pattern);
    }

    @Override
    public void delete(String id)
    {
        partsDAOimpl.deletePart( intFromString.recognize( id ) );
    }

    @Override
    public void changeEnabledStatus(String id)
    {
        partsDAOimpl.changeEnabledStatus( intFromString.recognize( id ) );
    }

    @Override
    public void update(Part part)
    {
        partsDAOimpl.updatePart( part );
    }

    @Override
    public void add(Part part)
    {
        partsDAOimpl.addPart( part );
    }

    /**
     * Изменение данных имеющейся в базе запчасти.
     */
    @Override
    public void update(String updateID, String updateTitle, boolean saveEnabled, String updateAmount)
    {
        Part part = new Part();
        part.setId      ( intFromString.recognize( updateID ) );
        part.setTitle   ( updateTitle     );
        part.setEnabled ( saveEnabled     );
        part.setAmount  ( intFromString.recognize(updateAmount) );
        update(part);
    }

}
