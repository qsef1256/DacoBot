import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static net.qsef1256.dacobot.DacoBot.logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JPATest {

    private static final DaoCommonJpa<AccountEntity, Long> dao = new DaoCommonJpaImpl<>(AccountEntity.class);

    @Test
    public void testJPA() throws Exception {
        logger.info("User Count: " + dao.count());

        final AccountEntity testUser = new AccountEntity();
        testUser.setDiscord_id(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");

        logger.info("is Exists?: " + dao.existsById(testUser.getDiscord_id()));
        logger.info("Before save: " + testUser.getDiscord_id().toString());

        dao.save(testUser);
        assertEquals(String.valueOf(419761037861060620L), testUser.getDiscord_id().toString());
        logger.info("After save: " + testUser.getDiscord_id().toString());
        logger.info(dao.toString());

        dao.close();
    }

}
