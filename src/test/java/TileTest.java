import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * TileTest
 */
public class TileTest {
    private <T extends Block> List<Block> makeBlockList(Class<T> classType, int size) {
        List<Block> blocks = new ArrayList<>();
        try {
            for (int i = 0; i < size; i++) {
                blocks.add(classType.newInstance());
            }
        } catch (IllegalAccessException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        }
        return blocks;
    }

    /**
     * Tests the Tile() constructor. Should initialise with no exits and
     * soil, soil and grass as starting blocks.
     */
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

    /**
     * Tests the Tile(List<Block>) constructor. Tests it stores the starting
     * blocks correctly and enforces height limits on blocks correctly.
     */
    @Test
    public void testConstructor2() throws Exception {
        // Often in these test methods, there is code which could throw but
        // shouldn't. Such code is included bare in a test method which
        // throws Exception. Any exception outside a try/catch fails the test.

        List<Block> blocks = makeBlockList(GrassBlock.class, 3);

        Tile t = new Tile(blocks); // Testing 3 starting ground blocks.
        assertEquals("Incorrect blocks.", blocks, t.getBlocks());

        blocks.add(new WoodBlock());
        // Modifying the parameter list should not affect the tile's blocks.
        assertEquals("Modifying list modified tile's blocks.",
            3, t.getBlocks().size());

        t = new Tile(blocks); // Testing 3 ground blocks, 1 non-ground.

        blocks.remove(blocks.size()-1);
        blocks.add(new GrassBlock());
        try {
            t = new Tile(blocks);
            fail("4 grass (ground) blocks, didn't throw.");
        } catch (TooHighException e) {}

        blocks.remove(blocks.size()-1);
        for (int i = 0; i < 5; i++) {
            blocks.add(new WoodBlock());
        }
        t = new Tile(blocks); // Tests 3 ground blocks and 5 non-ground.

        blocks.add(new WoodBlock());
        try {
            t = new Tile(blocks);
            fail("3 grass blocks, 6 wood blocks, didn't throw.");
        } catch (TooHighException e) {}
    }

    @Test
    public void testExits() throws Exception {
        Tile t = new Tile();

        try {
            t.addExit(null, t);
            fail("Null exit name should throw but didn't.");
        } catch (NoExitException e) {}

        try {
            t.addExit("exit name", null);
            fail("Null target tile should throw but didn't.");
        } catch (NoExitException e) {}

        Tile other = new Tile();
        t.addExit("name", other);
        assertTrue("New exit not stored.", t.getExits().containsKey("name"));
        assertEquals("New exit is the wrong tile.", other, t.getExits().get("name"));

        Tile newTarget = new Tile();
        t.addExit("name", newTarget);
        assertEquals("Target with same name not overwritten.",
                     newTarget, t.getExits().get("name"));
    }

    @Test
    public void testGetBlocks() throws Exception {
        List<Block> b = Arrays.asList((Block)new SoilBlock(), new WoodBlock());
        Tile t = new Tile(b);
        assertEquals("Incorrect blocks.", b, t.getBlocks());
    }

    @Test
    public void testGetExits() throws NoExitException {
        Tile t = new Tile();
        t.addExit("up", t);
        t.addExit("down", t);

        Map<String, Tile> m = new HashMap<>();
        m.put("down", t);
        m.put("up", t);

        assertEquals("Incorrect exits.", m, t.getExits());
    }

    @Test
    public void testGetTopBlock() throws Exception {
        Tile t = new Tile(new ArrayList<Block>());

        try {
            t.getTopBlock();
            fail("Get top block of empty tile didn't throw.");
        } catch (TooLowException e) {}

        Block top = new WoodBlock();
        t = new Tile(Arrays.asList((Block)new SoilBlock(), top));
        assertEquals("Incorrect top block.", top, t.getTopBlock());
    }

    @Test
    public void testRemoveTopBlock() throws Exception {
        Tile t = new Tile(new ArrayList<Block>());

        try {
            t.removeTopBlock();
            fail("Remove top block of empty tile didn't throw.");
        } catch (TooLowException e) {}

        List<Block> blockList = Arrays.asList(
            (Block)new SoilBlock(), new GrassBlock());

        Block bottomBlock = blockList.get(0);
        t = new Tile(blockList);
        t.removeTopBlock();
        assertEquals(
            "Top block incorrectly removed.",
            Arrays.asList(bottomBlock), t.getBlocks()
        );
    }

    @Test
    public void testRemoveExit() throws Exception {
        Tile t = new Tile();
        t.addExit("up", t);
        t.addExit("right", t);

        try {
            t.removeExit(null);
            fail("Removing null exit didn't throw.");
        } catch (NoExitException e) {}

        try {
            t.removeExit("down");
            fail("Removing non-existent exit didn't throw.");
        } catch (NoExitException e) {}

        Map<String, Tile> expected = new HashMap<String,Tile>();
        expected.put("right", t);
        t.removeExit("up");
        assertEquals("Exit not removed correctly.", expected, t.getExits());
    }

    @Test
    public void testDig() throws Exception {
        Tile t = new Tile(new ArrayList<Block>());
        try {
            t.dig();
            fail("Digging empty tile didn't throw.");
        } catch (TooLowException e) {}

        t = new Tile();
        t.placeBlock(new StoneBlock());
        try {
            t.dig();
            fail("Digging undiggable block didn't throw.");
        } catch (InvalidBlockException e) {}

        List<Block> blocks = Arrays.asList(
            (Block)new WoodBlock(), new SoilBlock());
        t = new Tile(blocks);
        Block b = t.dig();
        assertEquals("Digging removed incorrect block.", blocks.get(1), b);
        assertEquals(
            "Digging resulted in the incorrect blocks.",
            Arrays.asList(blocks.get(0)), t.getBlocks());
    }

    @Test
    public void testPlaceBlock() throws Exception {
        Tile t = new Tile();
        try {
            t.placeBlock(null);
            fail("Placing null block didn't throw.");
        } catch (InvalidBlockException e) {}
        try {
            t.placeBlock(new SoilBlock());
            fail("Placing ground block above height 3 didn't throw.");
        } catch (TooHighException e) {}

        for (int i = 0; i < 5; i++) {
            t.placeBlock(new WoodBlock()); // Could throw, but shouldn't.
        }
        try {
            t.placeBlock(new WoodBlock());
            fail("Placing normal block above 8 didn't throw.");
        } catch (TooHighException e) {}
        try {
            t.placeBlock((GroundBlock)new SoilBlock());
            fail("Placing ground block above 8 didn't throw.");
        } catch (TooHighException e) {}

        Block startingBlock = new StoneBlock();
        t = new Tile(Arrays.asList(startingBlock));

        Block newBlock = new WoodBlock();
        t.placeBlock(newBlock);
        assertEquals(
            "Block not placed correctly.",
            Arrays.asList(startingBlock, newBlock), t.getBlocks());
    }

    @Test
    public void testMoveBlock() throws Exception {
        Tile tile = new Tile();
        Tile otherTile = new Tile();

        try {
            tile.moveBlock("non-existent");
            fail("Moving via non-existent exit didn't throw.");
        } catch (NoExitException e) {}

        try {
            tile.moveBlock(null);
            fail("Moving via null exit didn't throw.");
        } catch (NoExitException e) {}

        tile.addExit("test exit", otherTile);

        tile.placeBlock(new StoneBlock());
        try {
            tile.moveBlock("test exit");
            fail("Moving unmoveable block didn't throw.");
        } catch (InvalidBlockException e) {}

        Block blockToMove = new WoodBlock();
        tile.placeBlock(blockToMove);
        otherTile.placeBlock(new StoneBlock());
        tile.moveBlock("test exit"); // otherTile is 1 height lower here.
        assertNotEquals(
            "Block not moved from original tile.",
            blockToMove, tile.getTopBlock());
        assertEquals(
            "Block placed on new tile.",
            blockToMove, otherTile.getTopBlock());

        otherTile.placeBlock(new StoneBlock());
        try {
            tile.moveBlock("test exit");
            fail("Moving to equal height tile didn't throw.");
        } catch (TooHighException e) {}

        tile = new Tile(new ArrayList<Block>());
        otherTile = new Tile(new ArrayList<Block>());
        tile.addExit("test 2", otherTile);
        try {
            tile.moveBlock("test 2");
            fail("Moving from empty tile to empty tile didn't throw.");
        } catch (TooHighException e) {}

    }
}