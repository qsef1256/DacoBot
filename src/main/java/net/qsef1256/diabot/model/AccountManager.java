package net.qsef1256.diabot.model;

import com.sun.jdi.request.DuplicateRequestException;
import net.qsef1256.diabot.data.DiscordUserEntity;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.util.DiscordUtil;

import java.time.LocalDateTime;

import static net.qsef1256.diabot.DiaBot.logger;

public class AccountManager {

    protected static final DaoCommon<Long, DiscordUserEntity> dao = new DaoCommonImpl<>(DiscordUserEntity.class);

    /**
     * 다양한 기능을 사용하기 위해 유저 등록을 시도합니다.
     *
     * @param discord_id User's snowflake id
     */
    public static void register(final long discord_id) {
        try {
            if (dao.isExist(discord_id))
                throw new DuplicateRequestException(DiscordUtil.getNameAsTag(discord_id) + " 유저는 이미 등록 되어 있습니다.");
            DiscordUserEntity userData = new DiscordUserEntity();
            userData.setDiscord_id(discord_id);
            userData.setRegisterTime(LocalDateTime.now());
            userData.setStatus("OK");
            dao.create(userData);
        } catch (DuplicateRequestException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 유저 등록에 실패했습니다");
        }
    }

}
