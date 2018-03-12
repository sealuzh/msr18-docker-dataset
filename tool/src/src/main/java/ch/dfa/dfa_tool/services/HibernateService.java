package ch.dfa.dfa_tool.services;

import ch.dfa.dfa_tool.models.Project;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

/**
 * Created by salizumberi-laptop on 21.11.2016.
 */
public class HibernateService {
    public static final SessionFactory SESSION_FACTORY;


    /**
     * Initialize the SessionFactory instance.
     */
    static {
        // Create a Configuration object.
        Configuration config = new Configuration();
        // Configure using the application resource named hibernate.cfg.xml.
        config.configure();
        // Extract the properties from the configuration file.
        Properties prop = config.getProperties();

        // Create StandardServiceRegistryBuilder using the properties.
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(prop);

        // Build a ServiceRegistry
        ServiceRegistry registry = builder.build();

        // Create the SessionFactory using the ServiceRegistry
        SESSION_FACTORY = config.buildSessionFactory(registry);
    }

    public static synchronized void save(Project project) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            session.clear();
            session.saveOrUpdate(project);

            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }


    public static void upateDockerfile(Project project) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            // Get the Diff from the database.
            Project oldProject = null;
            oldProject = (Project) session.get(Project.class,
                    project.getId());

            // Update the diff
            session.clear();
            session.update(oldProject);

            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }

    /**
     * Read all the Students.
     *
     * @return a List of Students
     */
    public static boolean doesDockerFileExist(Session s, String repo_path, String docker_path) {
        String queryPart1 = "SELECT count(*) FROM Dockerfile ";
        String queryPart2 = "WHERE REPO_PATH = '" + repo_path +"' ";
        String queryPart3 = "AND docker_path = '" + docker_path +"'";
        String query =  queryPart1 + queryPart2 + queryPart3;

        Session session = s;
        Transaction transaction = null;
        int count = 0;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            count = ((Long)session.createQuery(query).uniqueResult()).intValue();

            //int count = session.createQuery(queryPart1 + queryPart2 + queryPart3 + queryPart4).uniqueResult()();
            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
           // ex.printStackTrace();
        } finally {
            // Close the session
           // session.close();
        }
        if(count >0){
            return true;
        }else{
            return false;
        }
       // return dockerfiles;
    }
}
