package ru.javarush.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javarush.db.entity.Part;
import ru.javarush.service.PartsService;

import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Controller
public class PartsController
{
    PartsService partsService = new PartsService();

    List<Part> parts = partsService.getAllParts();

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String partsListWithFilters(
            @RequestParam(value = "sortBy",      required = false) String sortBy,
            Model model )
    {
        model.addAttribute("parts",     parts   );
        model.addAttribute("beginInt",  0    );
        model.addAttribute("endInt",    10   );
        model.addAttribute("page",      1   );

        return "index";
    }

}
