package ru.javarush.service.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Распознание запрошенной пользователем страницы с данными.
 *
 * @author Victor Bugaenko
 * @since 19.09.2018
 */

@Component
public class PageFromStringImpl implements PageFromString
{

    IntFromString intFromString = new IntFromStringImpl();
    /**
     * страница со списком данных не может быть 0.
     */

    public int recognize(String str)
    {
        int i = intFromString.recognize(str);
        if (i <= 0)
            return 1;
        else
            return i;
    }


}
