package ru.javarush.db.dao;

import ru.javarush.db.entity.Part;

import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

public interface PartsDAO
{
    void dropDB                  ();
    void dataForDB               ();
    void deletePart              ( int id     );
    void addPart                 ( Part part  );
    void updatePart              ( Part part  );
    List<Part> getParts          ( String sql );
    void changeEnabledStatus     ( int id     );
    int getHowManyRecordsByQuery ();
}
