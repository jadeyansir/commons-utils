package top.jadeyan.commons.object;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.*;

@SuppressWarnings("all")
public class StringExtensionUtilsTest {

    @Test
    public void testGetFirstNotBlank() {
        String[] strings = new String[]{"", " ", null, "1", "2"};
        Optional<String> firstNotBlank = StringExtensionUtils.getFirstNotBlank(strings);
        String result = firstNotBlank.get();
        assertEquals("1", result);

        String[] emptyStrings = new String[]{null, ""};
        Optional<String> firstNotBlank2 = StringExtensionUtils.getFirstNotBlank(emptyStrings);
        assertFalse(firstNotBlank2.isPresent());
    }

    @Test
    public void testGetDomain() {
        String test1 = "http://www.google.com/test";
        String test2 = "http://192.168.1.1:8080/test?a=中国";
        String test3 = "http://r.qa.innodealing.com/test";
        String test4 = "www.google.com";

        Optional<String> domainNameOptional1 = StringExtensionUtils.getDomainName(test1);
        Optional<String> domainNameOptional2 = StringExtensionUtils.getDomainName(test2);
        Optional<String> domainNameOptional3 = StringExtensionUtils.getDomainName(test3);
        Optional<String> domainNameOptional4 = StringExtensionUtils.getDomainName(test4);

        assertEquals("google.com", domainNameOptional1.get());
        assertEquals("192.168.1.1", domainNameOptional2.get());
        assertEquals("r.qa.innodealing.com", domainNameOptional3.get());
        assertEquals("google.com", domainNameOptional4.get());
    }

    @Test
    public void testToSafeString() {
        final String result1 = StringExtensionUtils.toSafeString(null);
        assertNull(result1);
        final String result2 = StringExtensionUtils.toSafeString(1);
        assertEquals("1", result2);
        final String result3 = StringExtensionUtils.toSafeString(null, true);
        assertEquals("", result3);
    }

    @Test
    public void testJoinWith() {
        assertEquals(StringExtensionUtils.objectJoinWith(",", "a", "b", null, "c"), "a,b,c");
    }

    @Test
    public void testBlankToDefault() {
        String target = "test";
        String defaultValue = "defaultValue";
        final String result1 = StringExtensionUtils.blankToDefault(target, defaultValue);
        assertEquals(target, result1);
        target = " ";
        final String result2 = StringExtensionUtils.blankToDefault(target, defaultValue);
        assertEquals(defaultValue, result2);
    }

    @Test
    public void testEmptyToDefault() {
        String target = "test";
        String defaultValue = "defaultValue";
        final String result1 = StringExtensionUtils.blankToDefault(target, defaultValue);
        assertEquals(target, result1);
        target = "";
        final String result2 = StringExtensionUtils.blankToDefault(target, defaultValue);
        assertEquals(defaultValue, result2);
    }

    @Test
    public void testIfNotBlank() {
        String target = "test";
        StringExtensionUtils.ifNotBlank(target, a -> assertEquals(target, a));
        StringExtensionUtils.ifNotBlank(" ", a -> Assert.fail());
        StringExtensionUtils.ifNotBlank("", a -> Assert.fail());
        StringExtensionUtils.ifNotBlank(null, a -> Assert.fail());
    }

    @Test
    public void testIfNotEmpty() {
        String target = "test";
        StringExtensionUtils.ifNotBlank(target, a -> assertEquals(target, a));
        StringExtensionUtils.ifNotBlank(" ", a -> assertEquals(target, a));
        StringExtensionUtils.ifNotBlank("", a -> Assert.fail());
        StringExtensionUtils.ifNotBlank(null, a -> Assert.fail());
    }
}
