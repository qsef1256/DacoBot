package net.qsef1256.dacobot.service.account.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.service.account.data.UserEntity;
import net.qsef1256.dacobot.service.account.exception.DacoAccountException;
import net.qsef1256.dacobot.util.JDAUtil;

import java.util.NoSuchElementException;

// TODO: OAuth 2.0
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    protected static final DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class);

    @Getter
    private UserEntity data;

    public Account(final long discordId) {
        try {
            if (!dao.existsById(discordId))
                throw new NoSuchElementException(JDAUtil.getNameAsTag(discordId) + " 유저는 등록되지 않았습니다.");
            data = dao.findById(discordId);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (final RuntimeException e) {
            e.printStackTrace();
            throw new DacoAccountException(JDAUtil.getNameAsTag(discordId) + " 의 정보를 로드하는데 실패했습니다");
        }
    }

}
