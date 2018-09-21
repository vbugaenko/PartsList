package ru.javarush.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.javarush.db.entity.Part;
import ru.javarush.service.PartsService;
import ru.javarush.service.PartsServiceImpl;
import ru.javarush.service.utility.IntFromStringImpl;

import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Controller
public class PartsController
{
    private PartsService partsService = new PartsServiceImpl();
    private IntFromStringImpl intFromString = new IntFromStringImpl();

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String partsListWithFilters(
            @RequestParam(value = "page",         required = false) String page,
            @RequestParam(value = "deleteID",     required = false) String deleteID,
            @RequestParam(value = "activateID",   required = false) String activateID,
            @RequestParam(value = "editID",       required = false) String editID,
            @RequestParam(value = "updateID",     required = false) String updateID,
            @RequestParam(value = "updateTitle",  required = false) String updateTitle,
            @RequestParam(value = "updateAmount", required = false) String updateAmount,
            @RequestParam(value = "saveEnabled",  required = false) boolean saveEnabled,
            @RequestParam(value = "searchTitle",  required = false) String searchTitle,
            @RequestParam(value = "filterField",  required = false) String filter,
            @RequestParam(value = "addNewPart",   required = false) String addNewPart,
            @RequestParam(value = "addTitle",     required = false) String addTitle,
            @RequestParam(value = "addEnabled",   required = false) String addEnabled,
            @RequestParam(value = "addAmount",    required = false) String addAmount,
            Model model )
    {
        if ((activateID != null)&&(!activateID.equals("")))
            partsService.changeEnabledStatus( activateID );

        if ((updateID != null)&&(!updateID.equals("")))
            partsService.update(updateID, updateTitle,saveEnabled, updateAmount);

        if ((deleteID != null)&&(!deleteID.equals("")))
            partsService.delete( deleteID );

        if ((addTitle != null)&&(!addTitle.equals("")))
            partsService.add( addTitle, addEnabled, addAmount );

        List<Part> parts = partsService.getParts( filter, searchTitle );

        /**
         * begin и end позволяют варьировать количество отображаемых записей на странице.
         */
        int limit = 10;
        int pageInt = 1;
        if ((page != null)&&(!page.equals("")))
            pageInt = intFromString.recognize( page );
        //TODO: перенести begin и end в JSP
        int begin = (pageInt-1)*limit;
        int end = begin+limit-1;

        /**
         * Подсчет числа компьютеров, которые можно собрать из имеющихся запчастей.
         * Осуществляется через поиск наименьшего числа деталей в перечне.
         */
        int min = 0;
        for(Part p : parts)
            if (p.isEnabled() &&( (min == 0)||(p.getAmount() < min)) )
                    min = p.getAmount();


        model.addAttribute("parts",       parts     );
        model.addAttribute("beginInt",    begin     );
        model.addAttribute("endInt",      end       );
        model.addAttribute("page",        pageInt   );
        model.addAttribute("sborka",      min       );
        model.addAttribute("editIDInt",   editID    );  //TODO: по хорошему это все в JSP может через JS
                                                          // и вообще можно уронить приложение, если там не число будет
        model.addAttribute("filter",      partsService.filerEnum(filter) );
        model.addAttribute("addNewPart",  addNewPart);
        model.addAttribute("searchTitle", searchTitle);
        return "index";
    }

}
