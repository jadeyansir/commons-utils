package top.jadeyan.commons.net;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class IpUtilsTest {

    @Test
    public void testIpToString() {
        String ip = "164.13.12.1";
        final long longValue = IpUtils.ipToLong(ip);
        final String s = IpUtils.longToIp(longValue);
        assertEquals(ip, s);
    }
}
