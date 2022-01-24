package net.qsef1256.diabot.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.diabot.data.DiscordUserData;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.util.DiscordUtil;

import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscordUser {

    protected final DaoCommon<Long, DiscordUserData> dao = new DaoCommonImpl<>(DiscordUserData.class);

    @Getter
    private DiscordUserData data;

    public DiscordUser(final long discord_id) {
        try {
            if (!dao.isExist(discord_id))
                throw new NoSuchElementException(DiscordUtil.getNameAsTag(discord_id) + " 유저는 등록되지 않았습니다.");
            data = dao.findById(discord_id);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (final RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 의 정보를 로드하는데 실패했습니다");
        }
    }

}
