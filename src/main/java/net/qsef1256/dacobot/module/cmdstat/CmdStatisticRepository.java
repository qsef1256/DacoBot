package net.qsef1256.dacobot.module.cmdstat;

import net.qsef1256.dacobot.module.cmdstat.data.CmdStatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmdStatisticRepository extends JpaRepository<CmdStatisticEntity, String> {

}
