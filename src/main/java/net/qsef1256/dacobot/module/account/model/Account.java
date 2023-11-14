package net.qsef1256.dacobot.module.account.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.module.account.data.UserEntity;
import net.qsef1256.dacobot.module.account.exception.DacoAccountException;
import net.qsef1256.dacobot.util.JDAService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

// TODO: OAuth 2.0
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    protected static final DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class);

    @Setter(onMethod_ = {@Autowired})
    private JDAService jdaService;
    private UserEntity data;

    public Account(final long discordId) {
        try {
            if (!dao.existsById(discordId))
                throw new NoSuchElementException(jdaService.getNameAsTag(discordId) + " 유저는 등록되지 않았습니다.");
            data = dao.findById(discordId);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (final RuntimeException e) {
            throw new DacoAccountException(jdaService.getNameAsTag(discordId) + " 의 정보를 로드하는데 실패했습니다", e);
        }
    }

}
