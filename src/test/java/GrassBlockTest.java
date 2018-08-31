import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * GrassBlockTest
 */
public class GrassBlockTest {
    GrassBlock grass;

    @Before
    public void setup() {
        grass = new GrassBlock();
    }

    @Test
    public void testGetColour() {
        assertEquals("Grass is not green.", "green", grass.getColour());
    }

    @Test
    public void testGetBlockType() {
        assertEquals("Grass is not grass.", "grass", grass.getBlockType());
    }

    @Test
    public void testIsCarryable() {
        assertFalse("Grass shouldn't be carryable.", grass.isCarryable());
    }

    // Testing the abstract class GroundBlock.

    @Test
    public void testIsMoveable() {
        assertFalse("Grass (ground block) shouldn't be moveable.",
                grass.isMoveable());
    }

    @Test
    public void testIsDiggable() {
        assertTrue("Grass (ground block) should be diggable.",
                grass.isDiggable());
    }
}