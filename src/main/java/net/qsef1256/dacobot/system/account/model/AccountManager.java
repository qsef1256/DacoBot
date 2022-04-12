package net.qsef1256.dacobot.system.account.model;

import com.sun.jdi.request.DuplicateRequestException;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonHibernateImpl;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.util.JDAUtil;

import java.time.LocalDateTime;

import static net.qsef1256.dacobot.DacoBot.logger;

public class AccountManager {

    protected static final DaoCommon<AccountEntity, Long> dao = new DaoCommonHibernateImpl<>(AccountEntity.class);

    /**
     * 다양한 기능을 사용하기 위해 유저 등록을 시도합니다.
     *
     * @param discordId User's snowflake id
     */
    public static void register(final long discordId) {
        try {
            if (dao.existsById(discordId))
                throw new DuplicateRequestException(JDAUtil.getNameAsTag(discordId) + " 유저는 이미 등록 되어 있습니다.");
            AccountEntity userData = new AccountEntity();
            userData.setDiscord_id(discordId);
            userData.setRegisterTime(LocalDateTime.now());
            userData.setStatus("OK");
            dao.save(userData);
        } catch (DuplicateRequestException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(JDAUtil.getNameAsTag(discordId) + " 유저 등록에 실패했습니다");
        }
    }

    /**
     * 계정을 삭제합니다.
     *
     * @param discordId User's snowflake id
     */
    public static void delete(final long discordId) {
        try {
            if (!dao.existsById(discordId))
                throw new DuplicateRequestException(JDAUtil.getNameAsTag(discordId) + " 계정은 이미 삭제 되었습니다.");
            dao.deleteById(discordId);
        } catch (DuplicateRequestException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(JDAUtil.getNameAsTag(discordId) + " 계정 삭제에 실패했습니다.");
        }
    }

    public static AccountEntity getAccount(long discordId) {
        return dao.findById(discordId);
    }

}
