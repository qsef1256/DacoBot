package net.qsef1256.diabot.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;

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

}
