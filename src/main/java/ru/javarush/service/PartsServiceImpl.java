package ru.javarush.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javarush.db.dao.PartsDAO;
import ru.javarush.db.entity.Part;
import ru.javarush.service.utility.IntFromString;

import java.util.List;

import static ru.javarush.service.PartsService.FilterEnum.ACTIVE;
import static ru.javarush.service.PartsService.FilterEnum.DISABLED;
import static ru.javarush.service.PartsService.FilterEnum.NONE;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Service
public class PartsServiceImpl implements PartsService
{
    private final Logger loggerFileInf = Logger.getLogger("fileinf");
    private final Logger loggerConsoleInf = Logger.getLogger("consoleinf");

    @Autowired
    private PartsDAO partsDAO;
    @Autowired
    private IntFromString intFromString;
    private List<Part> parts;
    private int page = 1;
    private int pagesCalc = 1;
    private FilterEnum filter = NONE;

    /**
     * переменная отвечает за количество отображаемых записей с запчастями на странице.
     */
    private int limit = 10;

    /**
     * Метод распознает текущий фильтр (из строки в enum).
     */
    private FilterEnum recognizeCurrentFilter(String filter)
    {
        if ((filter!=null)&&(filter.equals("ACTIVE"))) return ACTIVE;
        else if ((filter!=null)&&(filter.equals("DISABLED"))) return DISABLED;
        else return NONE;
    }

    /**
     * Метод меняет фильтр на следующий (NONE → ACTIVE → DISABLED → NONE).
     */
    private FilterEnum setNewFilter()
    {
        if (filter == NONE)   return ACTIVE;
        if (filter == ACTIVE) return DISABLED;
        else return NONE;
    }

    /**
     * Фильтрация (enabled) действует и по результатам поиска, и при движении по страницам;
     */
    @Override
    public List<Part> getParts(String currentFilter, String newFilter, String search, String pageStr)
    {
        filter = recognizeCurrentFilter(currentFilter);

        if ((newFilter != null)&&(!newFilter.equals("")))
            filter = setNewFilter();

        recognizePage(pageStr);

        parts = partsDAO.getParts("SELECT SQL_CALC_FOUND_ROWS * FROM part " + where() + searchStr(search)+" LIMIT " + begin() + ", "+limit+";");

        pagesCalc = (int)(Math.ceil(partsDAO.pagesCalc()/10.0));
        return parts;
    }

    /**
     * Метод нужен чтобы определить, какой диапазон записей из БД показывать на странице.
     * @param pageStr номер страницы
     */
    private void recognizePage(String pageStr)
    {
        if ((pageStr != null)&&(!pageStr.equals("")))
            page = intFromString.recognize( pageStr );
    }

    /**
     * В зависимости от значения переменной filter метод вернет часть sql запроса
     * для фильтрации данных при отборе их из БД.
     */
    private String where()
    {
        if (filter == ACTIVE)
            return "WHERE (enabled=1)";
        else if (filter == DISABLED)
            return "WHERE (enabled=0)";
        else
            return "WHERE (enabled=1 OR enabled=0) ";
    }

    /**
     * Если входящая строка (@param search) содержит какие-то данные,
     * то метод вернет (@return) часть sql запроса для их поиска в БД.
     */
    private String searchStr(String search)
    {
        String searchStr="";
        if ((search != null)&&(!search.equals("")))
            searchStr="AND title REGEXP '"+search+"'";
        return searchStr;
    }

    @Override
    public void delete(String id)
    {
        if ((id != null)&&(!id.equals("")))
            partsDAO.deletePart(intFromString.recognize(id));
    }

    @Override
    public void add(Part part) {
        partsDAO.addPart(part);
    }

    /**
     * Метод добавляет новую запчасть в БД.
     * @param addTitle название запчасти
     * @param addEnabled ее необходимость для (текущей) сборки
     * @param addAmount количество в наличии
     */
    @Override
    public void add(String addTitle, String addEnabled, String addAmount)
    {
        if ((addTitle != null)&&(!addTitle.equals("")))
        {
            Part part = new Part();
            part.setTitle(addTitle);
            if ((addEnabled != null) && (addEnabled.equals("on")))
                part.setEnabled(true);
            if ((addAmount != null) && (!addAmount.equals("")))
                part.setAmount(intFromString.recognize(addAmount));
            add(part);
        }
    }

    @Override
    public void changeEnabledStatus(String id)
    {
        if ((id != null)&&(!id.equals("")))
            partsDAO.changeEnabledStatus(intFromString.recognize(id));
    }

    @Override
    public void update(Part part) {
        partsDAO.updatePart(part);
    }

    @Override
    public void update(String updateID, String updateTitle, boolean saveEnabled, String updateAmount)
    {
        if ((updateID != null)&&(!updateID.equals("")))
        {
            Part part = new Part();
            part.setId(intFromString.recognize(updateID));
            part.setTitle(updateTitle);
            part.setEnabled(saveEnabled);
            part.setAmount(intFromString.recognize(updateAmount));
            update(part);
        }
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

    private int begin()
    {
        return (page-1)*limit;
    }

    public int getPagesCalc()
    {
        return pagesCalc;
    }

    public FilterEnum getFilter() { return filter; }

    public void drop(String dropOrder)
    {
        if ((dropOrder != null)&&(!dropOrder.equals("")))
        partsDAO.dropDB();
    }
}
