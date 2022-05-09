package top.jadeyan.commons.object;

import top.jadeyan.commons.encrypt.MD5Utils;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class MD5UtilsTest {

    @Test
    public void testGetMD5String() {
        String md5Code = MD5Utils.getMD5String("深圳桑达房地产开发有限公司");
        assertEquals(md5Code, "54790cf2a5607de0c374ce3f09acaecb");
    }
}
