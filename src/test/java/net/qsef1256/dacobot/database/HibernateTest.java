package net.qsef1256.dacobot.database;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.module.account.data.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
class HibernateTest {

    public void DaoCheck() {
        DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class);

        log.info("qsef1256 is exist?: " + dao.existsById(419761037861060619L));
        if (!dao.existsById(419761037861060620L))
            save(dao);
        find(dao);
        dao.deleteById(419761037861060620L);
        long count = dao.count();

        log.info("count: " + count);
    }

    private void find(@NotNull DaoCommon<UserEntity, Long> dao) {
        UserEntity user = dao.findById(419761037861060620L);
        log.info(user.getDiscordId() + " Status: " + user.getStatus() + " Time: " + user.getRegisterTime());
    }

    private void save(@NotNull DaoCommon<UserEntity, Long> dao) {
        UserEntity testUser = new UserEntity();
        testUser.setDiscordId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");

        log.info("Creating user: " + testUser.getDiscordId());
        dao.save(testUser);
    }

    @Test
    void testHibernate() {
        assertDoesNotThrow(this::DaoCheck);
    }

}
