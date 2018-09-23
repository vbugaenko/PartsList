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
    private final Logger loggerFileInf = Logger.getLogger("fileinf");
    private final Logger loggerConsoleInf = Logger.getLogger("consoleinf");
    private int pagesCalc=1;

    @Override
    public void dropDB()
    {
        try ( Session session = cfg.buildSessionFactory().openSession() )
        {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS part CASCADE;").executeUpdate();
            session.createSQLQuery(
            "CREATE TABLE part(id INT(11) NOT NULL AUTO_INCREMENT, PRIMARY KEY (id)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;")
            .executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e)
        { loggerFileInf.error(e.getMessage()); }
        dataForDB();
    }

    @Override
    public void dataForDB()
    {
        try ( Session session = cfg.buildSessionFactory().openSession() )
        {
            session.beginTransaction();
            for (int i = 0; i < 250; i++)
            session.createSQLQuery("INSERT INTO part (title, enabled, amount) VALUES (:title, :enabled, :amount);")
                .setParameter("title", "part_"+(100+i))
                .setParameter("enabled", enabled())
                .setParameter("amount", rnd()).executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e)
        { loggerFileInf.error(e.getMessage()); }
    }

    private boolean enabled()
    {
        return rnd() > 50 ? true : false;
    }

    private int rnd()
    {
        return (int) (Math.random() * 100);
    }

    @Override
    public void deletePart(int id)
    {
        try (Session session = cfg.buildSessionFactory().openSession())
        {
            session.beginTransaction();
            Part part = session.get(Part.class, id);
            session.remove(part);
            session.getTransaction().commit();
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
        }
        catch(Exception e)
        { loggerFileInf.error(e.getMessage()); }
    }

    @Override
    public void updatePart(Part part)
    {
        try (Session session = cfg.buildSessionFactory().openSession())
        {
            session.beginTransaction();
            session.update(part);
            session.getTransaction().commit();
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
