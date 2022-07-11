package net.qsef1256.dacobot.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;

/**
 * 캐스팅으로 뭔가 변환하는 유틸리티입니다.
 *
 * @param <T> Original Type
 * @param <S> Convert Type
 */
public class ConvertUtil<T, S> {

    private final Class<? extends T> T;
    private final Class<? extends S> S;

    public ConvertUtil(Class<? extends T> T, Class<? extends S> S) {
        this.T = T;
        this.S = S;
    }

    @SuppressWarnings("unchecked")
    public S[] array(@NotNull T[] array) {
        S[] result = (S[]) Array.newInstance(S, array.length);

        for (int i = 0; i < array.length; i++) {
            result[i] = (S) array[i];
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public S[][] matrix(@NotNull T[][] matrix) {
        S[][] result = (S[][]) Array.newInstance(S, matrix.length, matrix[0].length);
        for (int i = 0; i < matrix.length; i++) {
            result[i] = (S[]) Array.newInstance(S, matrix[i].length);
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = (S) matrix[i][j];
            }
        }
        return result;
    }

}
