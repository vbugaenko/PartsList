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
import ru.javarush.service.utility.PageFromString;
import ru.javarush.service.utility.PageFromStringImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Controller
public class PartsController
{
    private PartsService partsService = new PartsServiceImpl();

    private PageFromString pageFromString = new PageFromStringImpl();

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String partsListWithFilters(
            //@RequestParam(value = "filter",      required = false) String filter,
            @RequestParam(value = "page",         required = false) String page,
            @RequestParam(value = "deleteID",     required = false) String deleteID,
            @RequestParam(value = "activateID",   required = false) String activateID,
            @RequestParam(value = "editID",       required = false) String editID,
            @RequestParam(value = "updateID",     required = false) String updateID,
            @RequestParam(value = "updateTitle",  required = false) String updateTitle,
            @RequestParam(value = "updateAmount", required = false) String updateAmount,
            @RequestParam(value = "saveEnabledStatusForUpdate", required = false) boolean saveEnabledStatusForUpdate,
            @RequestParam(value = "searchTitle", required = false) String searchTitle,
            Model model )
    {

/*
        if (filter != null)
            usersListSortedFiltered.setFilter(filterFromString.recognize(filter.trim()));
*/

        int updateAmountInt=0;
        if ((updateAmount != null)&&(!updateAmount.equals("")))
        {
            updateAmountInt = new IntFromStringImpl().recognize(updateAmount);
        }

        String updateTitleStr="";
        if ((updateTitle != null)&&(!updateTitle.equals("")))
        {
            updateTitleStr=updateTitle;
        }

        int updateIDInt = 0;
        if ((updateID != null)&&(!updateID.equals("")))
        {
            updateIDInt = new IntFromStringImpl().recognize(updateID);
            Part part = new Part();
            part.setId      ( updateIDInt     );
            part.setTitle   ( updateTitle     );
            part.setEnabled ( saveEnabledStatusForUpdate );
            part.setAmount  ( updateAmountInt );
            partsService.update(part);
        }

        String editIDInt = null;
        if ((editID != null)&&(!editID.equals("")))
        {
            editIDInt = editID;
        }

        if ((activateID != null)&&(!activateID.equals("")))
        {
            int id = new IntFromStringImpl().recognize(activateID);
            partsService.changeEnabledStatus(id);
        }

        if ((deleteID != null)&&(!deleteID.equals("")))
        {
            int id = new IntFromStringImpl().recognize(deleteID);
            partsService.delete(id);
        }


        int limit = 10;
        int pageInt = 1;
        if ((page != null)&&(!page.equals("")))
            pageInt = pageFromString.recognize( page );
        int begin = (pageInt-1)*limit;
        int end =   begin+limit-1;


        List<Part> parts =  partsService.getAllParts();
        if ((searchTitle != null)&&(!searchTitle.equals("")))
        {
            parts = new ArrayList();
            Pattern pt = Pattern.compile( searchTitle, Pattern.CASE_INSENSITIVE );
            Matcher mt;

            for(Part p : partsService.getAllParts())
            {
                mt = pt.matcher(p.getTitle());
                if (mt.find())
                parts.add(p);
            }
        }

        int min = 0;
        for(Part p : partsService.getAllParts())
            if (p.isEnabled())
            {
                if ((min == 0)||(p.getAmount() < min))
                    min = p.getAmount();
            }


        model.addAttribute("parts",       parts     );
        model.addAttribute("beginInt",    begin     );
        model.addAttribute("endInt",      end       );
        model.addAttribute("page",        pageInt   );
        model.addAttribute("sborka",      min       );
        model.addAttribute("editIDInt",   editIDInt );

        return "index";
    }

}
