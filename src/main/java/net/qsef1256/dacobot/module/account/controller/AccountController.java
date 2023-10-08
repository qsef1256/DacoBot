package net.qsef1256.dacobot.module.account.controller;

import lombok.experimental.UtilityClass;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.module.account.data.UserEntity;
import net.qsef1256.dacobot.module.account.exception.DacoAccountException;
import net.qsef1256.dacobot.util.JDAUtil;

import java.time.LocalDateTime;

import static net.qsef1256.dacobot.DacoBot.logger;

@UtilityClass
public class AccountController {

    private static final DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class);

    /**
     * 다양한 기능을 사용하기 위해 유저 등록을 시도합니다.
     *
     * @param discordId User's snowflake id
     */
    public static void register(final long discordId) {
        try (dao) {
            dao.open();

            if (dao.existsById(discordId))
                throw new DacoAccountException(JDAUtil.getNameAsTag(discordId) + " 유저는 이미 등록 되어 있습니다.");

            UserEntity userData = new UserEntity();
            userData.setDiscordId(discordId);
            userData.setRegisterTime(LocalDateTime.now());
            userData.setStatus("OK");

            dao.save(userData);
        } catch (DacoAccountException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());

            throw new DacoAccountException(JDAUtil.getNameAsTag(discordId) + " 유저 등록에 실패했습니다");
        }
    }

    /**
     * 계정을 삭제합니다.
     *
     * @param discordId User's snowflake id
     */
    public static void delete(final long discordId) {
        try (dao) {
            dao.open();

            if (!dao.existsById(discordId))
                throw new DacoAccountException(JDAUtil.getNameAsTag(discordId) + " 계정은 이미 삭제 되었습니다.");
            dao.deleteById(discordId);
        } catch (DacoAccountException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());

            throw new DacoAccountException(JDAUtil.getNameAsTag(discordId) + " 계정 삭제에 실패했습니다.");
        }
    }

    public static UserEntity getAccount(long discordId) {
        dao.open();
        UserEntity entity = dao.findById(discordId);
        dao.close();

        return entity;
    }

}
