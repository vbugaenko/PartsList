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

        int updateAmountInt=0;
        if ((updateAmount != null)&&(!updateAmount.equals("")))
        {
            updateAmountInt = new IntFromStringImpl().recognize(updateAmount);
        }

        if ((updateID != null)&&(!updateID.equals("")))
        {
            Part part = new Part();
            part.setId      ( intFromString.recognize( updateID ) );
            part.setTitle   ( updateTitle     );
            part.setEnabled ( saveEnabled     );
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
            int id = intFromString.recognize(activateID);
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
            pageInt = intFromString.recognize( page );
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

            List<Part> partsTmp = new ArrayList();
            if ((filter==null)||(filter.equals("DISABLED")))
                filter = "NONE";
            else if (filter.equals("NONE"))
            {
                filter = "ACTIVE";
                for(Part p : parts)
                    if (p.isEnabled())
                        partsTmp.add(p);
                parts = partsTmp;
            }
            else if (filter.equals("ACTIVE"))
            {
                filter = "DISABLED";
                for (Part p : parts)
                    if (!p.isEnabled())
                        partsTmp.add(p);
                parts = partsTmp;
            }
            else
                filter = "NONE";

        //Добавление
            Part part = new Part();
            if ((addTitle != null)&&(!addTitle.equals("")))
            {
                part.setTitle(addTitle);

                if ((addEnabled != null)&&(!addEnabled.equals(""))&&(addEnabled.equals("on")))
                    part.setEnabled(true);
                else
                    part.setEnabled(false);

                if ((addAmount != null) && (!addAmount.equals("")))
                    part.setAmount(new IntFromStringImpl().recognize(addAmount));
                else
                    part.setAmount(0);

                partsService.add(part);
            }


        /**
         * Подсчет числа компьютеров, которые можно собрать из имеющихся запчастей.
         * Осуществляется через поиск наименьшего числа деталей в перечне.
         */
        int min = 0;
        for(Part p : parts)
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
        model.addAttribute("filter",      filter    );
        model.addAttribute("addNewPart",  addNewPart);
        return "index";
    }

}
