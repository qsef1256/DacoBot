package net.qsef1256.dacobot.module.account.command;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.module.account.controller.AccountController;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import net.qsef1256.dacobot.module.account.model.Account;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@SpringBootTest
class AttendCommandTest {

    @BeforeEach
    void registerUser(@Autowired AccountController controller) {
        controller.register(419761037861060620L);
    }

    @AfterEach
    void deleteUser(@Autowired AccountController controller) {
        controller.delete(419761037861060620L);
    }

    @Test
    void testAttend() {
        try (DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class)) {
            Account user = new Account(419761037861060620L);
            UserEntity userData = user.getData();

            LocalDateTime lastAttendTime = userData.getLastAttendTime();
            if (lastAttendTime != null && LocalDateTimeUtil.isToday(lastAttendTime)) {
                log.info("이미 출석했습니다. 출석 시간: " + LocalDateTimeUtil.getTimeString(lastAttendTime));
            } else {
                userData.setLastAttendTime(LocalDateTime.now());
                userData.setAttendCount(userData.getAttendCount() + 1);
                dao.save(userData);

                log.info("출석 체크! 정상적으로 출석 체크 되었습니다.\n\n" + "출석 횟수: " + userData.getAttendCount());
            }

        } catch (RuntimeException e) {
            log.error("오류 발생", e.getMessage());

            if (e instanceof NoSuchElementException) return;
            e.printStackTrace();
        }
    }

}
