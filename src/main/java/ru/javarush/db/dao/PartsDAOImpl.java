package ru.javarush.db.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import ru.javarush.db.entity.Part;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Repository
public class PartsDAOImpl implements PartsDAO
{
    private Configuration cfg = new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml");
    final Logger loggerFileInf = Logger.getLogger("fileinf");
    final Logger loggerConsoleInf = Logger.getLogger("consoleinf");

    @Override
    public Part getByTitle(String title)
    {
        Part part = null;
        try (Session session = cfg.buildSessionFactory().openSession())
        {
            part = session.get(Part.class, title);
            session.getTransaction().commit();
        }
        catch (Exception e)
        { loggerConsoleInf.error( e.getMessage() ); }
        return part;
    }

    @Override
    public List<Part> getAllParts()
    {
        System.out.println("Выдать данные о всех запчастях");
        List<Part> parts = new ArrayList();
        try ( Session session = cfg.buildSessionFactory().openSession() )
        {
            NativeQuery query = session.createNativeQuery("SELECT * FROM parts;");
            query.addEntity(Part.class);
            parts = query.list();
            session.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            //loggerFileInf.error(e.getMessage());
        }
        return parts;
    }
}
