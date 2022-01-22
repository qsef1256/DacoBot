package net.qsef1256.diabot.model;

import lombok.Getter;
import net.qsef1256.diabot.data.DiscordUserData;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.database.MainDAO;
import net.qsef1256.diabot.database.MainDAOImpl;
import net.qsef1256.diabot.util.DiscordUtil;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public class DiscordUser {
    
    protected final DaoCommon<DiscordUser, Long> daoCommon = new DaoCommonImpl<>(DiscordUser.class);

    protected final MainDAO dao = new MainDAOImpl();
    @Getter
    private DiscordUserData data;

    private DiscordUser() {
    }

    public DiscordUser(final long discord_id) throws SQLException {
        try {
            if (!dao.isUserExist(discord_id))
                throw new NoSuchElementException(DiscordUtil.getNameAsTag(discord_id) + " 유저는 등록되지 않았습니다.");
            data = dao.getUser(discord_id);
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new SQLException(DiscordUtil.getNameAsTag(discord_id) + " 의 정보를 로드하는데 실패했습니다");
        }
    }

}
