package net.glowstone.util.mojangson;

import net.glowstone.util.nbt.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestMojangsonWrite {

    // The tag to write, the expected output.
    private final Tag tag;
    private final String expected;

    public TestMojangsonWrite(Tag tag, String expected) {
        this.tag = tag;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getCases() {
        return Arrays.asList(
                new Object[]{new ByteTag((byte) 1), "{value:1b}"},
                new Object[]{new DoubleTag((double) 1), "{value:1.0d}"},
                new Object[]{new FloatTag((float) 1), "{value:1.0f}"},
                new Object[]{new IntTag(1), "{value:1}"},
                new Object[]{new ListTag<>(TagType.STRING, Arrays.asList(new StringTag("1"), new StringTag("2"))), "{value:[\"1\",\"2\"]}"},
                new Object[]{new LongTag((long) 1), "{value:1l}"},
                new Object[]{new ShortTag((short) 1), "{value:1s}"},
                new Object[]{new StringTag("1"), "{value:\"1\"}"}
        );
    }

    @Test
    public void canWriteTag() {
        CompoundTag top = new CompoundTag();
        top.getValue().put("value", tag);
        String result = Mojangson.fromTag(top);
        Assert.assertEquals("Could not write case for " + tag.getType().getName() + ": Wrong output.", expected, result);
    }

}
