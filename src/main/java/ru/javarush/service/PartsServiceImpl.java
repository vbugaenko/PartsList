package ru.javarush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javarush.db.dao.PartsDAO;
import ru.javarush.db.entity.Part;
import ru.javarush.service.utility.IntFromString;

import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Service
public class PartsServiceImpl implements PartsService
{
    @Autowired
    private PartsDAO partsDAO;
    @Autowired
    private IntFromString intFromString;
    private List<Part> parts;
    private int page = 1;
    private int pagesCalc=1;
    private Filter filter = Filter.NONE;


    /**
     * limit позволяет варьировать количеством отображаемых записей на странице.
     */
    private int limit = 10;


    /**
     * @param filter принимается текущее значение
     * @return выставляется следующее
     */
    private Filter currentFilter(String filter)
    {
        if ((filter==null)||(filter.equals("NONE")))
            return Filter.NONE;
        if (filter.equals("ACTIVE"))
            return Filter.ACTIVE;
        else if (filter.equals("DISABLED"))
            return Filter.DISABLED;

        return Filter.NONE;
    }

    private Filter newFilter()
    {
        if (filter == Filter.NONE)
            return Filter.ACTIVE;
        if (filter == Filter.ACTIVE)
            return Filter.DISABLED;
        else
            return Filter.NONE;
    }

    /**
     * Фильтрация (enabled) действует и по результатам поиска, и при движении по страницам;
     */
    @Override
    public List<Part> getParts(String currentFilter, String newFilter, String search, String pageStr)
    {
        filter = currentFilter(currentFilter);

        if ((newFilter != null)&&(!newFilter.equals("")))
            filter = newFilter();


        if ((pageStr != null)&&(!pageStr.equals("")))
            page = intFromString.recognize( pageStr );

        String searchStr="";
        if ((search != null)&&(!search.equals("")))
            searchStr="AND title REGEXP '"+search+"'";

        if (filter == Filter.ACTIVE)
            parts = partsDAO.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM parts WHERE enabled=1 "+searchStr+" LIMIT " + begin() + ", "+limit+";");
        else if (filter == Filter.DISABLED)
            parts = partsDAO.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM parts WHERE enabled=0 "+searchStr+" LIMIT " + begin() + ", "+limit+";");
        else
            parts = partsDAO.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM parts WHERE enabled=1 OR enabled=0 "+searchStr+" LIMIT "+ begin() +", "+limit+";");

        pagesCalc = (int)(Math.ceil(partsDAO.pagesCalc()/10.0));
        return parts;
    }

    @Override
    public void delete(String id) {
        partsDAO.deletePart(intFromString.recognize(id));
    }

    @Override
    public void add(Part part) {
        partsDAO.addPart(part);
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
        partsDAO.changeEnabledStatus(intFromString.recognize(id));
    }

    @Override
    public void update(Part part) {
        partsDAO.updatePart(part);
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

    public Filter getFilter() { return filter; }

}
