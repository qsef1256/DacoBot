package net.qsef1256.diabot.util;

import java.lang.reflect.Array;

public class MatrixUtil<T> {

    private final Class<? extends T> clazz;

    public MatrixUtil(Class<? extends T> cls) {
        this.clazz = cls;
    }

    public int getWidth(final T[][] matrix) {
        return matrix.length > 0 ? matrix[0].length : 0;
    }

    public int getHeight(final T[][] matrix) {
        return matrix.length;
    }

    public T[][] resizeHorizon(T[][] matrix, int x) {
        return resize(matrix, x, getHeight(matrix));
    }

    public T[][] resizeVertical(T[][] matrix, int y) {
        return resize(matrix, getWidth(matrix), y);
    }

    @SuppressWarnings("unchecked")
    public T[][] resize(T[][] matrix, int width, int height) {
        T[][] result = (T[][]) Array.newInstance(clazz, height, width);

        for (int y = 0; y < Math.min(height,getHeight(matrix)); y++) {
            if (Math.min(width, getWidth(matrix)) >= 0)
                System.arraycopy(matrix[y], 0, result[y], 0, Math.min(width, getWidth(matrix)));
        }
        return result;
    }

    public static void main(String[] args) {
        MatrixUtil<Integer> matrixUtil = new MatrixUtil<>(Integer.class);

        Integer[][] matrix = new Integer[8][8];
        matrix = matrixUtil.resize(matrix,6,9);

        System.out.println("width: " + matrixUtil.getWidth(matrix) + " height: " + matrixUtil.getHeight(matrix));

        matrix = matrixUtil.resize(matrix,14,9);

        System.out.println("width: " + matrixUtil.getWidth(matrix) + " height: " + matrixUtil.getHeight(matrix));
    }

}

