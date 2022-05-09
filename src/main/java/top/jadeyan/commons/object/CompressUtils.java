package top.jadeyan.commons.object;


import top.jadeyan.commons.exception.IORuntimeException;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 字符串扩展工具类
 *
 * @author yan
 */
public final class CompressUtils {

    private CompressUtils() {
    }

    private static final int BYTE_BUFFER_SIZE = 1024;

    /**
     * 压缩数组
     *
     * @param bytes 数组
     * @return 压缩后的数组
     */
    public static byte[] compress(byte[] bytes) {
        Deflater deflater = new Deflater();
        deflater.setInput(bytes);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length)) {
            deflater.finish();
            byte[] buffer = new byte[BYTE_BUFFER_SIZE];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new IORuntimeException("compress error", ex);
        }
    }

    /**
     * 解压数组
     *
     * @param compressedBytes 压缩后的数组
     * @return 数组
     */
    public static byte[] decompress(byte[] compressedBytes) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedBytes);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedBytes.length)) {
            byte[] buffer = new byte[BYTE_BUFFER_SIZE];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new IORuntimeException("decompress error", ex);
        }
    }
}
