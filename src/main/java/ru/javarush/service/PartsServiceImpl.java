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
    private enum Filter { NONE, DISABLED, ACTIVE }
    private List<Part> parts;

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

    /**
     * В зависимости от фильтра или наличия задания на поиск используется разный sql запрос.
     */
    @Override
    public List<Part> getParts(String filter, String search )
    {
        if ((search != null)&&(!search.equals("")))
            parts = partsDAOimpl.getParts("SELECT * FROM parts WHERE title REGEXP '"+search+"';");
        else if (filerEnum(filter) == Filter.ACTIVE)
            parts = partsDAOimpl.getParts("SELECT * FROM parts WHERE enabled=1;");
        else if (filerEnum(filter) == Filter.DISABLED)
            parts = partsDAOimpl.getParts("SELECT * FROM parts WHERE enabled=0;");
        else
            parts = partsDAOimpl.getParts("SELECT * FROM parts;");
        return parts;
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

    /**
     * Подсчет числа компьютеров, которые можно собрать из имеющихся запчастей.
     * Осуществляется через поиск наименьшего числа деталей в перечне.
     */
    @Override
    public int min()
    {
        int min = 0;
        for(Part p : parts)
            if (p.isEnabled() &&( (min == 0)||(p.getAmount() < min)) )
                min = p.getAmount();
        return min;
    }

}
