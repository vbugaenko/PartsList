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
public class PartsServiceImpl implements PartsService {
    private PartsDAOImpl partsDAOimpl = new PartsDAOImpl();
    private IntFromStringImpl intFromString = new IntFromStringImpl();
    enum Filter { NONE, DISABLED, ACTIVE }

    /**
     * @param filter принимается текущее значение
     * @return выставляется следующее
     */
    public Enum filerEnum(String filter)
    {
        if ((filter==null)||(filter.equals("DISABLED")))
            return Filter.NONE;
        if (filter.equals("NONE"))
            return Filter.ACTIVE;
        else if (filter.equals("ACTIVE"))
            return Filter.DISABLED;

        return Filter.NONE;
    }

    @Override
    public List<Part> getParts(String filter, String search )
    {
        if ((search != null)&&(!search.equals("")))
            return partsDAOimpl.getParts("SELECT * FROM parts WHERE title REGEXP '"+search+"';");

        if (filerEnum(filter) == Filter.ACTIVE)
            return partsDAOimpl.getParts("SELECT * FROM parts WHERE enabled=1;");
        if (filerEnum(filter) == Filter.DISABLED)
            return partsDAOimpl.getParts("SELECT * FROM parts WHERE enabled=0;");

        return partsDAOimpl.getParts("SELECT * FROM parts;");
    }

    @Override
    public void delete(String id) {
        partsDAOimpl.deletePart(intFromString.recognize(id));
    }

    @Override
    public void add(Part part) {
        partsDAOimpl.addPart(part);
    }

    @Override
    public void add(String addTitle, String addEnabled, String addAmount)
    {
        Part part = new Part();

        part.setTitle(addTitle);

        if ((addEnabled != null) && (addEnabled.equals("on")))
            part.setEnabled(true);

        if ((addAmount != null) && (!addAmount.equals("")))
            part.setAmount(intFromString.recognize(addAmount));

        add(part);
    }

    @Override
    public void changeEnabledStatus(String id) {
        partsDAOimpl.changeEnabledStatus(intFromString.recognize(id));
    }

    @Override
    public void update(Part part) {
        partsDAOimpl.updatePart(part);
    }

    @Override
    public void update(String updateID, String updateTitle, boolean saveEnabled, String updateAmount)
    {
        Part part = new Part();
        part.setId(intFromString.recognize(updateID));
        part.setTitle(updateTitle);
        part.setEnabled(saveEnabled);
        part.setAmount(intFromString.recognize(updateAmount));
        update(part);
    }

}
