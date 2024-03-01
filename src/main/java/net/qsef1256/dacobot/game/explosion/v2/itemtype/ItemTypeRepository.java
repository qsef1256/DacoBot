package net.qsef1256.dacobot.game.explosion.v2.itemtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTypeRepository extends JpaRepository<ItemTypeEntity, Integer> {

}
