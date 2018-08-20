import org.junit.Assert;
import org.junit.Test;

/**
 * GrassBlockTest
 */
public class GrassBlockTest {
    @Test
    public void testConstructor() {
        GrassBlock grass = new GrassBlock();
    }

    @Test
    public void testGetColour() {
        GrassBlock grass = new GrassBlock();
        Assert.assertEquals("Grass is not green.", "green", grass.getColour());
    }

    @Test
    public void testGetBlockType() {
        GrassBlock grass = new GrassBlock();
        Assert.assertEquals("Grass is not grass.", "grass", grass.getBlockType());
    }

    @Test
    public void testIsCarryable() {
        GrassBlock grass = new GrassBlock();
        Assert.assertFalse("Grass shouldn't be carryable.", grass.isCarryable());
    }
}