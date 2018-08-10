import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class BuilderTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testConstructor1()
    {
        Tile t = new Tile();
        Builder b = new Builder("name", t);
        assertEquals("name", b.getName());
        assertEquals(t, b.getCurrentTile());
    }

    @Test
    public void testConstructorExceptions() {
        Tile t = new Tile();
        List<Block> l = new ArrayList<Block>();
        l.add(new StoneBlock());
        
        boolean thrown = false;
        try {
            Builder b = new Builder("name", t, l);
        } catch (InvalidBlockException e) {
            thrown = true;
        }
        assertTrue("Uncarryable block should throw.", thrown);
        thrown = false;
        
        l.clear();
        l.add(new WoodBlock());
        try {
            Builder b = new Builder("name", t, l);
        } catch (InvalidBlockException e) {
            thrown = true;
        }
        assertFalse("Carryable block shouldn't throw.", thrown);
    }
}
