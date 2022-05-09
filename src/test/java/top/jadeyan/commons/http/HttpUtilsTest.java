package top.jadeyan.commons.http;

import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class HttpUtilsTest {

    @Test
    public void testDecodeUIComponent() {
        String value = "fzK1c3umcmS6dHVjPjKCR1OGV2NjMDKicHdjPjKGV%7BJ2OjK%3A%2FfzK%7BeXJjPjJ2NkV1NkdjMDKqZYRjPkF3OENyO%7BZzNUhtJnW5dDJ7NUZ1N%7BJ3NkZyPI1%2F%3AjcnqW%5BTxPoFRusN6Zt4rXDMr%3Ayse2lxE6KZvNq9f%7BC1hu%7BgvIeXiQMO18cI78h%7Bx%5BI4%7BtkbxewKVJd6o1V%5B%5Bx";
        String expect = "fzK1c3umcmS6dHVjPjKCR1OGV2NjMDKicHdjPjKGV{J2OjK:/fzK{eXJjPjJ2NkV1NkdjMDKqZYRjPkF3OENyO{ZzNUhtJnW5dDJ7NUZ1N{J3NkZyPI1/:jcnqW[TxPoFRusN6Zt4rXDMr:yse2lxE6KZvNq9f{C1hu{gvIeXiQMO18cI78h{x[I4{tkbxewKVJd6o1V[[x";
        final String result = HttpUtils.decodeURIComponent(value);
        assertEquals(expect, result);
    }

    @Test
    public void testEncodeUIComponent() {
        String value = "fzK1c3umcmS6dHVjPjKCR1OGV2NjMDKicHdjPjKGV{J2OjK:/fzK{eXJjPjJ2NkV1NkdjMDKqZYRjPkF3OENyO{ZzNUhtJnW5dDJ7NUZ1N{J3NkZyPI1/:jcnqW[TxPoFRusN6Zt4rXDMr:yse2lxE6KZvNq9f{C1hu{gvIeXiQMO18cI78h{x[I4{tkbxewKVJd6o1V[[x";
        String expect = "fzK1c3umcmS6dHVjPjKCR1OGV2NjMDKicHdjPjKGV%7BJ2OjK%3A%2FfzK%7BeXJjPjJ2NkV1NkdjMDKqZYRjPkF3OENyO%7BZzNUhtJnW5dDJ7NUZ1N%7BJ3NkZyPI1%2F%3AjcnqW%5BTxPoFRusN6Zt4rXDMr%3Ayse2lxE6KZvNq9f%7BC1hu%7BgvIeXiQMO18cI78h%7Bx%5BI4%7BtkbxewKVJd6o1V%5B%5Bx";
        final String result = HttpUtils.encodeURIComponent(value);
        assertEquals(expect, result);
    }

    @Test
    public void testGetQueryParams() {
        String url = "http://www.baidu.com?u=yan&p=123456&token=fzK1c3umcmS6dHVjPjKCR1OGV2NjMDKicHdjPjKGV%7BJ2OjK%3A%2FfzK%7BeXJjPjJ2NkV1NkdjMDKqZYRjPkF3OENyO%7BZzNUhtJnW5dDJ7NUZ1N%7BJ3NkZyPI1%2F%3AjcnqW%5BTxPoFRusN6Zt4rXDMr%3Ayse2lxE6KZvNq9f%7BC1hu%7BgvIeXiQMO18cI78h%7Bx%5BI4%7BtkbxewKVJd6o1V%5B%5Bx";
        final Map<String, String> queryParams = HttpUtils.getQueryParams(url);
        assertEquals("yan", queryParams.get("u"));
        assertEquals("123456", queryParams.get("p"));
        assertEquals("fzK1c3umcmS6dHVjPjKCR1OGV2NjMDKicHdjPjKGV{J2OjK:/fzK{eXJjPjJ2NkV1NkdjMDKqZYRjPkF3OENyO{ZzNUhtJnW5dDJ7NUZ1N{J3NkZyPI1/:jcnqW[TxPoFRusN6Zt4rXDMr:yse2lxE6KZvNq9f{C1hu{gvIeXiQMO18cI78h{x[I4{tkbxewKVJd6o1V[[x",
                queryParams.get("token"));
    }

    @Test
    public void testGetValueFromHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "1");
        List<Map.Entry<String, String>> headers = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            headers.add(entry);
        }
        final Optional<String> testOptional = HttpUtils.getValueFromHeaders(headers, "test");
        assertEquals("1", testOptional.get());
    }
}
