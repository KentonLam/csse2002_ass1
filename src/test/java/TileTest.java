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
        assertEquals("Not 3 initial blocks.", 3, b.size());
        // We can't use assertEquals() with a List<Block> because .equals()
        // for Block compares references, not types.
        assertTrue("First block not soil.", b.get(0) instanceof SoilBlock);
        assertTrue("Second block not soil.", b.get(1) instanceof SoilBlock);
        assertTrue("Third block not grass.", b.get(2) instanceof GrassBlock);
        assertEquals("Initial exits exist.", 0, t.getExits().size());
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
        assertFalse("3 grass blocks threw.", thrown);

        startingBlocks.add(new WoodBlock());
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertFalse("3 grass blocks, 1 wood threw.", thrown);

        startingBlocks.remove(startingBlocks.size()-1);
        startingBlocks.add(new GrassBlock());
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertTrue("4 grass blocks, didn't throw.", thrown);
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
        assertFalse("3 grass blocks, 5 wood blocks threw.", thrown);

        startingBlocks.add(new WoodBlock());
        try {
            t = new Tile(startingBlocks);
        } catch (TooHighException e) {
            thrown = true;
        }
        assertTrue("3 grass blocks, 6 wood blocks, didn't throw.", thrown);
    }

    @Test
    public void testExits() throws NoExitException {
        Tile t = new Tile();

        boolean thrown = false;
        try {
            t.addExit(null, t);
        } catch (NoExitException e) {
            thrown = true;
        }
        assertTrue("Null exit name should throw but didn't.", thrown);
        thrown = false;

        try {
            t.addExit("exit name", null);
        } catch (NoExitException e) {
            thrown = true;
        }
        assertTrue("Null target tile should throw but didn't.", thrown);
        thrown = false;

        Tile other = new Tile();
        t.addExit("name", other);
        assertTrue("New exit not returned.", t.getExits().containsKey("name"));
        assertEquals("New exit is the wrong tile.", other, t.getExits().get("name"));

        Tile newTarget = new Tile();
        t.addExit("name", newTarget);
        assertEquals("Target with same name not overwritten.",
                     newTarget, t.getExits().get("name"));

    }

}