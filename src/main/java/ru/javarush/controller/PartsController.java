package ru.javarush.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javarush.db.entity.Part;

import java.util.List;
import ru.javarush.service.PartsServiceImpl;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Controller
public class PartsController
{
    PartsServiceImpl partsService = new PartsServiceImpl();

    List<Part> parts = partsService.getAllParts();

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String partsListWithFilters(
            @RequestParam(value = "sortBy",      required = false) String sortBy,
            @RequestParam(value = "filter",      required = false) String filter,
            @RequestParam(value = "page",        required = false) String page,
            @RequestParam(value = "idForUpdate", required = false) String idForUpdate,
            @RequestParam(value = "idForDelete", required = false) String idForDelete,
            @RequestParam(value = "activateID",  required = false) String activateID,
            @RequestParam(value = "editID",      required = false) String editID,
            @RequestParam(value = "editTitle",   required = false) String editTitle,
            @RequestParam(value = "editPrice",   required = false) String editPrice,
            @RequestParam(value = "editAmount",  required = false) String editAmount,
            Model model )
    {
        model.addAttribute("parts",     parts   );
        model.addAttribute("beginInt",  0    );
        model.addAttribute("endInt",    9   );
        model.addAttribute("page",      1   );

        return "index";
    }

}
