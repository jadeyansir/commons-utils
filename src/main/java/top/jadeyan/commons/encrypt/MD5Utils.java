package top.jadeyan.commons.encrypt;

import top.jadeyan.commons.exception.EncryptRuntimeException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密工具类
 *
 * @author yan
 * @date 2021/5/12 15:35
 */
public final class MD5Utils {

    private MD5Utils() {

    }

    /**
     * 获得字符串的md5值
     *
     * @param str 待加密的字符串
     * @return md5加密后的字符串
     */
    public static String getMD5String(String str) {
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptRuntimeException("MD5 algorithm not found", e);
        }
        return bytesToHex(bytes);
    }

    private static String bytesToHex(byte[] bytes) {
        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                result.append("0");
            }
            result.append(hex);
        }
        return result.toString();
    }

}
