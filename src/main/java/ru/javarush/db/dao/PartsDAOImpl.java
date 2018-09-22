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
    private int pagesCalc=1;

    @Override
    public void deletePart(int id)
    {
        try (Session session = cfg.buildSessionFactory().openSession())
        {
            session.beginTransaction();
            Part part = session.get(Part.class, id);
            session.remove(part);
            session.getTransaction().commit();
            session.close();
        }
        catch(Exception e)
        { loggerFileInf.error(e.getMessage()); }
    }

    @Override
    public void addPart(Part part)
    {
        try (Session session = cfg.buildSessionFactory().openSession())
        {
            session.beginTransaction();
            session.save(part);
            session.getTransaction().commit();
            session.close();
        }
        catch(Exception e)
        { loggerFileInf.error(e.getMessage()); }
    }

    @Override
    public void updatePart(Part newPart)
    {
        try (Session session = cfg.buildSessionFactory().openSession())
        {
            session.beginTransaction();
            session.update(newPart);
            session.getTransaction().commit();
            session.close();
        }
        catch(Exception e)
        { loggerFileInf.error(e.getMessage()); }
    }

    /**
     * Кроме основного запроса на отбор нужных данных,
     * используется и второй запрос на подсчет общего числа строк в БД соответвующих запросу.
     */
    @Override
    public List<Part> getParts(String sql)
    {
        List<Part> parts = new ArrayList();
        try ( Session session = cfg.buildSessionFactory().openSession() )
        {
            NativeQuery query = session.createNativeQuery( sql );
            query.addEntity(Part.class);
            parts = query.list();
            query = session.createNativeQuery( "SELECT FOUND_ROWS();" );

            //Todo: java.math.BigInteger cannot be cast to java.lang.Integer ??
            pagesCalc = Integer.parseInt(query.list().get(0).toString());
            session.close();
        }
        catch (Exception e)
        { loggerFileInf.error(e.getMessage()); }
        return parts;
    }

    @Override
    public void changeEnabledStatus(int id)
    {
        try (Session session = cfg.buildSessionFactory().openSession())
        {
            session.beginTransaction();
            Part part = session.get(Part.class, id);
            part.setEnabled(!part.isEnabled());
            session.update(part);
            session.getTransaction().commit();
            session.close();
        }
        catch(Exception e)
        { loggerFileInf.error(e.getMessage()); }
    }

    @Override
    public int pagesCalc()
    {
        return pagesCalc;
    }

}
