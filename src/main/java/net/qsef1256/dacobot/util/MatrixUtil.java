package net.qsef1256.dacobot.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MatrixUtil<T> {

    private final Class<? extends T> clazz;

    public MatrixUtil(Class<? extends T> cls) {
        this.clazz = cls;
    }

    /**
     * 2차원 배열의 너비를 구합니다.
     *
     * @param matrix matrix
     * @return matrix's width
     */
    public int getWidth(@NotNull T[][] matrix) {
        return matrix.length > 0 ? matrix[0].length : 0;
    }

    /**
     * 2차원 배열의 높이를 구합니다.
     *
     * @param matrix matrix
     * @return matrix's height
     */
    public int getHeight(@NotNull T[][] matrix) {
        return matrix.length;
    }

    public T[][] resizeHorizon(T[][] matrix, int x) {
        return resize(matrix, x, getHeight(matrix));
    }

    public T[][] resizeVertical(T[][] matrix, int y) {
        return resize(matrix, getWidth(matrix), y);
    }

    /**
     * 2차원 배열의 크기를 조정합니다. 빈 곳은 null 로 채워집니다.
     *
     * @param matrix Matrix to resize
     * @param width  width
     * @param height height
     * @return resized Matrix
     */
    @SuppressWarnings("unchecked")
    public T[][] resize(T[][] matrix, int width, int height) {
        T[][] result = (T[][]) Array.newInstance(clazz, height, width);

        for (int y = 0; y < Math.min(height, getHeight(matrix)); y++) {
            if (Math.min(width, getWidth(matrix)) >= 0)
                System.arraycopy(matrix[y], 0, result[y], 0, Math.min(width, getWidth(matrix)));
        }
        return result;
    }

    /**
     * 2차원 배열을 1차원 리스트로 만듭니다.
     *
     * @param matrix matrix
     * @return list
     * @see #flatMap(Object[][])
     */
    public List<T> toList(T[][] matrix) {
        return flatMap(matrix).toList();
    }

    /**
     * 2차원 배열을 1차원 배열로 만듭니다.
     *
     * @param matrix Matrix to 1d array
     * @return 1d array
     */
    @NotNull
    private Stream<T> flatMap(T[][] matrix) {
        return Arrays.stream(matrix).flatMap(Arrays::stream);
    }

    /**
     * 2차원 배열이 특정 값을 포함하고 있는지 확인합니다.
     *
     * @param matrix matrix
     * @param value  value for check
     * @return true when matrix contains value
     */
    public boolean anyMatch(T[][] matrix, T value) {
        return flatMap(matrix).anyMatch(v -> v == value);
    }

    /**
     * 2차원 배열이 특정 값으로 이루어져 있는지 확인합니다.
     *
     * @param matrix matrix
     * @param value  value for check
     * @return true when matrix all element is value
     */
    public boolean allMatch(T[][] matrix, T value) {
        return flatMap(matrix).allMatch(v -> v == value);
    }

    /**
     * 깊은 복사를 수행합니다.
     *
     * @param matrix matrix to copy
     * @return deep copy matrix
     */
    @SuppressWarnings("unchecked")
    public T[][] deepCopy(T[][] matrix) {
        T[][] copy = (T[][]) Array.newInstance(clazz, matrix.length, matrix[0].length);

        for (int i = 0; i < matrix.length; i++) {
            copy[i] = (T[]) Array.newInstance(clazz, matrix[i].length);

            System.arraycopy(matrix[i], 0, copy[i], 0, matrix[i].length);
        }
        return copy;
    }

}
