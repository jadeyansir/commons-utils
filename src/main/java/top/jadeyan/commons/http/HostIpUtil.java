package top.jadeyan.commons.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 获取IP工具
 *
 * @author yan
 */
public final class HostIpUtil {

    private static final Log logger = LogFactory.getLog(HostIpUtil.class);

    private HostIpUtil() {

    }

    @SuppressWarnings("java:S3740")
    public static String getLocalHostLANAddress() {
        List<String> ipList = new ArrayList<>();
        // 遍历所有的网络接口
        try {
            for (Enumeration interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration enumeration = networkInterface.getInetAddresses(); enumeration.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumeration.nextElement();
                    ipList.add(inetAddress.getHostAddress());
                }
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            ipList.add(jdkSuppliedAddress.getHostAddress());
        } catch (SocketException | UnknownHostException e) {
            logger.error(e);
        }
        for (String ip : ipList) {
            if (ip.contains("192.168")) {
                return ip;
            }
        }
        return "";
    }
}
