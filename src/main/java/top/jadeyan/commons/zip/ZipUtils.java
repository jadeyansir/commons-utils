package top.jadeyan.commons.zip;

import top.jadeyan.commons.exception.IORuntimeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/**
 * zip压缩工具类
 *
 * @author yan
 */
public final class ZipUtils {
    private ZipUtils() {
    }

    /**
     * 解压字符串,默认utf-8
     *
     * @param text 字符串
     * @return 解压后的字符串
     * @throws IORuntimeException 处理失败异常
     */
    public static String unzipBase64(String text) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try (OutputStream outputStream = new InflaterOutputStream(os)) {
                outputStream.write(Base64.getDecoder().decode(text));
            }
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 压缩字符串,默认梳utf-8
     *
     * @param text 字符串
     * @return 压缩后的字符串
     * @throws IORuntimeException 处理失败异常
     */
    public static String zipBase64(String text) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out)) {
                deflaterOutputStream.write(text.getBytes(StandardCharsets.UTF_8));
            }
            return new String(Base64.getEncoder().encode(out.toByteArray()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 压缩字节
     *
     * @param bytes 字节
     * @return 压缩后的字节
     */
    public static byte[] zipBytes(byte[] bytes) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length)) {
            try (DeflaterOutputStream outputStream = new DeflaterOutputStream(bos)) {
                outputStream.write(bytes);
            }
            return bos.toByteArray();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    /**
     * 解压字节
     *
     * @param bytes 字节
     * @return 解压后字节
     */
    public static byte[] unzipBytes(byte[] bytes) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length)) {
            try (InflaterOutputStream outputStream = new InflaterOutputStream(bos)) {
                outputStream.write(bytes);
            }
            return bos.toByteArray();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }
}
