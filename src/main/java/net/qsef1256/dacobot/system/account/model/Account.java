package net.qsef1256.dacobot.system.account.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.util.DiscordUtil;

import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    protected static final DaoCommon<Long, AccountEntity> dao = new DaoCommonImpl<>(AccountEntity.class);

    @Getter
    private AccountEntity data;

    public Account(final long discord_id) {
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
