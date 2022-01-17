package net.qsef1256.diabot.model;

import com.sun.jdi.request.DuplicateRequestException;
import net.qsef1256.diabot.data.DiscordUserData;
import net.qsef1256.diabot.database.MainDAO;
import net.qsef1256.diabot.database.MainDAOImpl;
import net.qsef1256.diabot.game.explosion.model.ExplosionGameCore;
import net.qsef1256.diabot.util.DiscordUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static net.qsef1256.diabot.DiaBot.logger;

public class DiscordManager {

    protected final MainDAO mainDAO = new MainDAOImpl();

    /**
     * 다양한 기능을 사용하기 위해 유저 등록을 시도합니다.
     *
     * @param discord_id User's snowflake id
     */
    public void register(final long discord_id) throws SQLException {
        try {
            if (mainDAO.isUserExist(discord_id))
                throw new DuplicateRequestException(DiscordUtil.getNameAsTag(discord_id) + " 유저는 이미 등록 되어 있습니다.");
            mainDAO.addUser(new DiscordUserData(discord_id, Timestamp.from(Instant.now()), "OK"));
            ExplosionGameCore.register(discord_id);
        } catch (final SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(DiscordUtil.getNameAsTag(discord_id) + " 유저 등록에 실패했습니다");
        }
    }

}
