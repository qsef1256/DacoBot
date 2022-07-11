package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class CollectionUtil {

    // TODO: for List and Collection

    /**
     * 순서에 상관 없이 같은지 비교합니다.
     *
     * @param list1 list 1
     * @param list2 list 2
     * @param <T>   Type of list
     * @return true when same regardless order
     */
    public <T> boolean isSame(Set<T> list1, Set<T> list2) {
        return new HashSet<>(list1).containsAll(list2) && list1.size() == list2.size();
    }

}
