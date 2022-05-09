package top.jadeyan.commons.elasticsearch;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ElasticsearchUtilsTest {

    @Test
    public void testListHighlightWords() {
        String target = "我爱<em>中国</em>天安<em>门</em>,天安<em>门</em>广场升国旗";
        final List<String> strings = ElasticsearchUtils.listHighlightWords(target);
        assertEquals(3, strings.size());
        assertEquals("中国", strings.get(0));
        assertEquals("门", strings.get(1));
        assertEquals("门", strings.get(2));
    }

    @Test
    public void testGetBestHighLight() {
        String highlight1 = "<em>16</em>仙桃城投债（1680160.IB）";
        String highlight2 = "<em>16</em><em>仙桃</em><em>城投</em>债（1680160.IB）";
        final String bestHighlight = ElasticsearchUtils.getBestHighlight(highlight1, highlight2).orElseGet(null);
        assertEquals(highlight2, bestHighlight);
    }
}
