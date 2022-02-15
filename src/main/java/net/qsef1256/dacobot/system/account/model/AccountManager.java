package net.qsef1256.dacobot.system.account.model;

import com.sun.jdi.request.DuplicateRequestException;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.util.DiscordUtil;

import java.time.LocalDateTime;

import static net.qsef1256.dacobot.DacoBot.logger;

public class AccountManager {

    protected static final DaoCommon<AccountEntity, Long> dao = new DaoCommonImpl<>(AccountEntity.class);

    /**
     * 다양한 기능을 사용하기 위해 유저 등록을 시도합니다.
     *
     * @param discord_id User's snowflake id
     */
    public static void register(final long discord_id) {
        try {
            if (dao.existsById(discord_id))
                throw new DuplicateRequestException(DiscordUtil.getNameAsTag(discord_id) + " 유저는 이미 등록 되어 있습니다.");
            AccountEntity userData = new AccountEntity();
            userData.setDiscord_id(discord_id);
            userData.setRegisterTime(LocalDateTime.now());
            userData.setStatus("OK");
            dao.save(userData);
        } catch (DuplicateRequestException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 유저 등록에 실패했습니다");
        }
    }

    /**
     * 계정을 삭제합니다.
     *
     * @param discord_id User's snowflake id
     */
    public static void delete(final long discord_id) {
        try {
            if (!dao.existsById(discord_id))
                throw new DuplicateRequestException(DiscordUtil.getNameAsTag(discord_id) + " 계정은 이미 삭제 되었습니다.");
            dao.deleteById(discord_id);
        } catch (DuplicateRequestException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 계정 삭제에 실패했습니다.");
        }
    }

    public static AccountEntity getAccount(long discord_id) {
        return dao.findById(discord_id);
    }

}
