package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * 2차원 배열의 Primitive <-> Object 타입 변환을 해줍니다.
 *
 * @see org.apache.commons.lang3.ArrayUtils
 * @see ConvertUtil
 */
@UtilityClass
public class PrimitiveUtil {

    public byte[][] toPrimitive(@NotNull Byte[][] matrix) {
        byte[][] result = new byte[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                result[y][x] = matrix[y][x];
            }
        }
        return result;
    }

    @NotNull
    public Byte[][] toObject(byte[][] matrix) {
        Byte[][] result = new Byte[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                result[y][x] = matrix[y][x];
            }
        }
        return result;
    }

}
