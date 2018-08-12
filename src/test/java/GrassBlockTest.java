import static org.junit.Assert.*;
import org.junit.Test;

/**
 * GrassBlockTest
 */
public class GrassBlockTest {
    @Test
    public void testConstructor() {
        GrassBlock g = new GrassBlock();
    }

    @Test
    public void testGetColour() {
        GrassBlock g = new GrassBlock();
        assertEquals("Grass is not green.", "green", g.getColour());
    }

    @Test
    public void testGetBlockType() {
        GrassBlock g = new GrassBlock();
        assertEquals("Grass is not grass.", "grass", g.getBlockType());
    }

    @Test
    public void testIsCarryable() {
        GrassBlock g = new GrassBlock();
        assertFalse("Grass shouldn't be carryable.", g.isCarryable());
    }
}