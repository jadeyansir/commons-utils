package top.jadeyan.commons.encrypt;

import org.junit.Assert;
import org.junit.Test;

/**
 * m5d加密测试
 *
 * @author yan
 * @date 2021/5/12 17:20
 */
public class MD5UtilsTest {

    @Test
    public void testGetMD5String() {
        String md5String = MD5Utils.getMD5String("123456");
        Assert.assertEquals("e10adc3949ba59abbe56e057f20f883e", md5String);
    }

}
