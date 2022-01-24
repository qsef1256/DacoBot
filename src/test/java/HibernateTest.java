import lombok.Getter;
import net.qsef1256.diabot.data.DiscordUserData;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

import static net.qsef1256.diabot.DiaBot.logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HibernateTest {
    @Getter
    private static EntityManagerFactory entityManagerFactory;

    protected void setUp() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("net.qsef1256.diabot");
        } catch (final Exception e) {
            entityManagerFactory.close();
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        entityManagerFactory.close();
    }

    public void save() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        final DiscordUserData testUser = new DiscordUserData();
        testUser.setId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("WHAT");

        logger.info("Before save: " + testUser.getId().toString());
        entityManager.persist(testUser);
        assertEquals(String.valueOf(419761037861060620L), testUser.getId().toString());
        logger.info("After save: " + testUser.getId().toString());
        logger.info(entityManager.getTransaction().toString());
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void get() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final List result = entityManager.createQuery("from DiscordUserData ").getResultList();
        for (final DiscordUserData user : (List<DiscordUserData>) result) {
            System.out.println("User (" + user.getId() + ") : " + user.getStatus());
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void DaoCheck() {
        DaoCommon<Long, DiscordUserData> dao = new DaoCommonImpl<>(DiscordUserData.class);

        logger.info("qsef1256 is exist?: " + dao.isExist(419761037861060619L));
        if (!dao.isExist(419761037861060620L))
            create(dao);
        find(dao);
        dao.deleteById(419761037861060620L);
    }

    private void find(DaoCommon<Long, DiscordUserData> dao) {
        DiscordUserData user = dao.findById(419761037861060620L);
        logger.info(user.getId() + " Status: " + user.getStatus() + " Time: " + user.getRegisterTime());
    }

    private void create(DaoCommon<Long, DiscordUserData> dao) {
        DiscordUserData testUser = new DiscordUserData();
        testUser.setId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");

        logger.info("Creating user: " + testUser.getId());
        dao.create(testUser);
    }

    @Test
    public void testHibernate() {
        DaoCheck();
    }
}
