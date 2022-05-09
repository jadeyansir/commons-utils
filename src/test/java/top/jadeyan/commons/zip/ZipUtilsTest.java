package top.jadeyan.commons.zip;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class ZipUtilsTest {

    @Test
    public void unzipBase64() {
        String text = "hello world";
        String s = ZipUtils.zipBase64(text);
        String s1 = ZipUtils.unzipBase64(s);
        Assert.assertEquals("两个相等", text, s1);
    }

    @Test
    public void zipBase64() {
        String text = "hello" +
                " \n \r \t " +
                "world 1111======\\v=======\\===111111111111111111111111111111111111111111113123!@$#@%$^%#&#$#E!@#!@!!%^TEWFSDV!@次!@" +
                "R!QEDs@!ED@df!#e!#dczdvcew";
        String s = ZipUtils.zipBase64(text);
        String s1 = ZipUtils.unzipBase64(s);
        Assert.assertEquals("两个相等", text, s1);
    }

    @Test
    public void testZipBytes() {
        String text = "hello world";
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        final byte[] zippedBytes = ZipUtils.zipBytes(bytes);
        final byte[] unzipBytes = ZipUtils.unzipBytes(zippedBytes);

        String result = new String(unzipBytes, StandardCharsets.UTF_8);
        Assert.assertEquals(result, text);

    }
}