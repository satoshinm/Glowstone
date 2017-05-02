package net.glowstone.util.mojangson;

import net.glowstone.util.mojangson.ex.MojangsonParseException;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.Tag;
import net.glowstone.util.nbt.TagType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestMojangsonParse {

    private final TagType tag;
    private final String mojangson;

    public TestMojangsonParse(TagType tag, String mojangson) {
        this.tag = tag;
        this.mojangson = mojangson;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getCases() {
        return Arrays.asList(
                new Object[]{TagType.BYTE, "{value:1b}"},
                new Object[]{TagType.DOUBLE, "{value:1.0}"},
                new Object[]{TagType.FLOAT, "{value:1.0f}"},
                new Object[]{TagType.INT, "{value:1}"},
                new Object[]{TagType.LIST, "{value:[\"1\",\"2\"]}"},
                new Object[]{TagType.LONG, "{value:1l}"},
                new Object[]{TagType.SHORT, "{value:1s}"},
                new Object[]{TagType.STRING, "{value:\"1\"}"}
        );
    }

    @Test
    public void canParseType() {
        try {
            CompoundTag compound = Mojangson.parseCompound(mojangson);
            Tag value = compound.getValue().get("value");

            // Checks if the TagType of the case and the parsed type are equal.
            if (value.getType() != tag) {
                Assert.fail("Incorrect type parsing for case " + tag.getName() + " (Got " + value.getType().getName() + ") for Mojansgon: " + mojangson);
            }
        } catch (MojangsonParseException e) {
            // Catches a parse failure.
            Assert.fail("Could not parse case for " + tag.getName() + "( " + mojangson + "): " + e.getMessage());
        }
    }
}
