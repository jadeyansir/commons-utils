package top.jadeyan.commons.object;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

@SuppressWarnings("all")
public class CollectionExtUtilsTest {

    @Test
    public void testGetFirstForNull() {
        final Optional<Object> first = CollectionExtUtils.getFirst(null);
        assertFalse(first.isPresent());
        final Optional<Object> first1 = CollectionExtUtils.getFirst(new ArrayList<>());
        assertFalse(first1.isPresent());
    }

    @Test
    public void TestGetFirst() {
        final ArrayList<Integer> nums = new ArrayList<>();
        nums.add(3);
        nums.add(2);
        nums.add(1);
        final Optional<Integer> first = CollectionExtUtils.getFirst(nums);
        assertEquals(3, (int) CollectionExtUtils.getFirst(nums).get());
    }
}
