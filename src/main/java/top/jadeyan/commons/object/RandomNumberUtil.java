package top.jadeyan.commons.object;

import top.jadeyan.commons.http.HostIpUtil;

import java.security.SecureRandom;

/**
 * 生成随机数工具
 *
 * @author yan
 */
public final class RandomNumberUtil {

    public static final int SCALE = 2;
    public static final int PAGE_INFO_NUMBER = 100;

    public static final int SPLIT_NUMBER = 500;

    private static final int RANDOM_NUMBER_RANGE = 9000;

    public static final int RANDOM_NUMBER_MINIMUM = 1000;

    private RandomNumberUtil() {

    }

    private static String ipToHex(String ip) {
        if ("".equals(ip)) {
            return "";
        }
        String[] ipNums = ip.split("\\.");
        StringBuilder hexIp = new StringBuilder();
        for (String ipNum : ipNums) {
            int a = Integer.parseInt(ipNum);
            String strHex = Integer.toHexString(a);
            hexIp.append(strHex);
        }
        return hexIp.toString();
    }

    /**
     * 生成随机序列号
     *
     * @return String
     */
    public static String randomNumber() {
        //4位随机数字+11位+16进制ip地址 8位(可为空)+时间戳+线程id 不限位数
        //随机数
        SecureRandom secureRandom = new SecureRandom();
        Integer randomNum = secureRandom.nextInt(RANDOM_NUMBER_RANGE) + RANDOM_NUMBER_MINIMUM;

        //十六进制 服务器ip
        String ip = HostIpUtil.getLocalHostLANAddress();
        String ipHex = ipToHex(ip);
        //13位时间戳
        return String.format("%d%s%d%d", randomNum, ipHex, System.currentTimeMillis(), Thread.currentThread().getId());
    }
}
