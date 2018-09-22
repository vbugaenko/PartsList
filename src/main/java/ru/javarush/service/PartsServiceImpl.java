package ru.javarush.service;

import org.springframework.stereotype.Service;
import ru.javarush.db.dao.PartsDAOImpl;
import ru.javarush.db.entity.Part;
import ru.javarush.service.utility.IntFromStringImpl;

import java.util.List;

/**
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
    private int page = 1;
    private int pagesCalc;

    /**
     * limit позволяет варьировать количеством отображаемых записей на странице.
     */
    private int limit = 10;


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
    public List<Part> getParts(String filter, String search, String pageStr)
    {
        if ((pageStr != null)&&(!pageStr.equals("")))
            page = intFromString.recognize( pageStr );

        if ((search != null)&&(!search.equals("")))
            parts = partsDAOimpl.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM parts ORDER BY title  WHERE title REGEXP '"+search+"' LIMIT "+ begin() +", 10;");
        else if (filerEnum(filter) == Filter.ACTIVE)
            parts = partsDAOimpl.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM parts ORDER BY title  WHERE enabled=1 LIMIT "+ begin() +", 10;");
        else if (filerEnum(filter) == Filter.DISABLED)
            parts = partsDAOimpl.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM parts ORDER BY title  WHERE enabled=0 LIMIT "+ begin() +", 10;");
        else
            parts = partsDAOimpl.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM parts ORDER BY title LIMIT "+ begin() +", 10;");

        pagesCalc = (int)(Math.ceil(partsDAOimpl.pagesCalc()/10.0));
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

    public int end()
    {
        return begin()+limit;
    }

    public int begin()
    {
        return (page-1)*limit;
    }

    public int getPagesCalc()
    {
        return pagesCalc;
    }

}
