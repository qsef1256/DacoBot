package net.qsef1256.dacobot.game.explosion.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTypeRepository extends JpaRepository<ItemTypeEntity, Long> {

}