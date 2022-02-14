import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

import static net.qsef1256.dacobot.DacoBot.logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HibernateTest {
    @Getter
    private static EntityManagerFactory entityManagerFactory;

    // Manually setup entityManager
    @Deprecated
    protected void setUp() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("net.qsef1256.dacobot");
        } catch (final Exception e) {
            entityManagerFactory.close();
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public void shutdown() {
        entityManagerFactory.close();
    }

    // Uses JDA native entityManager
    @Deprecated
    public void save() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        final AccountEntity testUser = new AccountEntity();
        testUser.setDiscord_id(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("WHAT");

        logger.info("Before save: " + testUser.getDiscord_id().toString());
        entityManager.persist(testUser);
        assertEquals(String.valueOf(419761037861060620L), testUser.getDiscord_id().toString());
        logger.info("After save: " + testUser.getDiscord_id().toString());
        logger.info(entityManager.getTransaction().toString());
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    // Uses JDA native entityManager
    @Deprecated
    public void get() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final List result = entityManager.createQuery("from AccountEntity").getResultList();
        for (final AccountEntity user : (List<AccountEntity>) result) {
            System.out.println("User (" + user.getDiscord_id() + ") : " + user.getStatus());
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void DaoCheck() {
        DaoCommon<AccountEntity, Long> dao = new DaoCommonImpl<>(AccountEntity.class);

        logger.info("qsef1256 is exist?: " + dao.existsById(419761037861060619L));
        if (!dao.existsById(419761037861060620L))
            create(dao);
        find(dao);
        dao.deleteById(419761037861060620L);
    }

    private void find(@NotNull DaoCommon<AccountEntity, Long> dao) {
        AccountEntity user = dao.findById(419761037861060620L);
        logger.info(user.getDiscord_id() + " Status: " + user.getStatus() + " Time: " + user.getRegisterTime());
    }

    private void create(@NotNull DaoCommon<AccountEntity, Long> dao) {
        AccountEntity testUser = new AccountEntity();
        testUser.setDiscord_id(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");

        logger.info("Creating user: " + testUser.getDiscord_id());
        dao.create(testUser);
    }

    @Test
    public void testHibernate() {
        DaoCheck();
    }
}
