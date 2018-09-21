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

    @Override
    public void delete(int id)
    {
        partsDAOimpl.deletePart( id );
    }

    @Override
    public void changeEnabledStatus(int id)
    {
        partsDAOimpl.changeEnabledStatus(id);
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
