package net.qsef1256.dacobot.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class JPAUtil {

    public <T> T getRandomRow(String tableName, @NotNull EntityManager em) {
        Query countQuery = em.createNativeQuery("select count(*) from " + tableName);
        long count = (Long) countQuery.getSingleResult();
        int number = RandomUtil.getRandom().nextInt((int) count);

        Query selectQuery = em.createQuery("select q from " + tableName + " q");
        selectQuery.setFirstResult(number);
        selectQuery.setMaxResults(1);

        return (T) selectQuery.getSingleResult();
    }

}
