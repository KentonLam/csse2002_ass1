import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * TileTest
 */
public class TileTest {

    @Test
    public void testConstructor1() {
        Tile t = new Tile();
        List<Block> b = t.getBlocks();
        assertEquals("3 initial blocks.", 3, b.size());
        // We can't use assertEquals() with a List<Block> because .equals()
        // for Block compares references, not types.
        assertTrue("First block soil.", b.get(0) instanceof SoilBlock);
        assertTrue("Second block soil.", b.get(1) instanceof SoilBlock);
        assertTrue("Third block grass.", b.get(2) instanceof GrassBlock);
        assertEquals("No initial exits.", 0, t.getExits().size());
    }

    @Test
    public void testConstructor2() {
        List<Block> startingBlocks = new ArrayList<Block>();
        startingBlocks.add(new GrassBlock());
        startingBlocks.add(new GrassBlock());
        startingBlocks.add(new GrassBlock());

        boolean thrown = false;
        Tile t;
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertFalse("3 grass blocks.", thrown);

        startingBlocks.add(new WoodBlock());
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertFalse("3 grass blocks, 1 wood.", thrown);

        startingBlocks.remove(startingBlocks.size()-1);
        startingBlocks.add(new GrassBlock());
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertTrue("4 grass blocks, should throw.", thrown);
        thrown = false;

        startingBlocks.remove(startingBlocks.size()-1);
        for (int i = 0; i < 5; i++) {
            startingBlocks.add(new WoodBlock());
        }
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertFalse("3 grass blocks, 5 wood blocks.", thrown);


        startingBlocks.add(new WoodBlock());
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertTrue("3 grass blocks, 6 wood blocks, should throw.", thrown);
    }    
}