package ru.javarush.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.javarush.service.PartsService;
import ru.javarush.service.PartsServiceImpl;
import ru.javarush.service.utility.IntFromStringImpl;
import ru.javarush.service.utility.PageFromString;
import ru.javarush.service.utility.PageFromStringImpl;

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
            //@RequestParam(value = "sortBy",      required = false) String sortBy,
            //@RequestParam(value = "filter",      required = false) String filter,
            @RequestParam(value = "page",        required = false) String page,
            @RequestParam(value = "deleteID",    required = false) String deleteID,
            @RequestParam(value = "activateID",  required = false) String activateID,
            //@RequestParam(value = "idForUpdate", required = false) String idForUpdate,
            //@RequestParam(value = "editTitle",   required = false) String editTitle,
            //@RequestParam(value = "editAmount",  required = false) String editAmount,
            Model model )
    {


/*
        if (sortBy != null)
            usersListSortedFiltered.setSortBy(sortByFromString.recognize(sortBy));

        if (filter != null)
            usersListSortedFiltered.setFilter(filterFromString.recognize(filter.trim()));

        if (role != null)
            usersListSortedFiltered.setRole(roleFromString.recognize(role));

        int idEdttUser = 0;
        if (editID != null)
            idEdttUser = intFromString.recognize(idForUpdate);

        if (idForUpdate != null)
            usersListService.update(editID, editInfo, editRole);
*/

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


        model.addAttribute("parts",     partsService.getAllParts() );
        model.addAttribute("beginInt",  begin   );
        model.addAttribute("endInt",    end     );
        model.addAttribute("page",      pageInt );

        return "index";
    }

}
