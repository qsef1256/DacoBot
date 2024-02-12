package net.qsef1256.dacobot.game.explosion.v2.cash;

import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashRepository extends JpaRepository<CashEntity, UserEntity> {

}