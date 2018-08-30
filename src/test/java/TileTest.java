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
    // We don't use a setup function because many test methods require
    // a specially constructed tile.

    /**
     * Helper method to generate a list of a certain type of block.
     *
     * @param <T> A type implementing Block.
     * @param classType Class type to use in list.
     * @param size Number of class instances required.
     */
    private static <T extends Block> List<Block> makeBlockList(
            Class<T> classType, int size) {

        List<Block> blocks = new ArrayList<>();
        try {
            for (int i = 0; i < size; i++) {
                blocks.add(classType.newInstance());
            }
        } catch (IllegalAccessException e) {
            // These should never happen because we will always pass a
            // valid constructor.
            return null;
        } catch (InstantiationException e) {
            return null;
        }
        return blocks;
    }

    @Test
    public void testConstructor1() {
        Tile tile = new Tile();
        List<Block> blocks = tile.getBlocks();

        assertEquals(
            "Not 3 initial blocks.", 3, blocks.size());
        // We can't use assertEquals() with a List<Block> because
        // .equals() for Block compares references, not types.
        assertTrue(
            "First block not soil.", blocks.get(0) instanceof SoilBlock);
        assertTrue(
            "Second block not soil.", blocks.get(1) instanceof SoilBlock);
        assertTrue(
            "Third block not grass.", blocks.get(2) instanceof GrassBlock);
        assertEquals(
            "Initial exits exist.", 0, tile.getExits().size());
    }

    @Test
    public void testConstructor2() throws Exception {
        // Often in these test methods, there is code which could throw but
        // shouldn't. Such code is included bare in a test method which
        // throws Exception. Any exception outside a try/catch fails the test.

        List<Block> blocks = makeBlockList(GrassBlock.class, 3);

        Tile tile = new Tile(blocks); // Testing 3 starting ground blocks.
        assertEquals("Incorrect blocks.", blocks, tile.getBlocks());

        blocks.add(new WoodBlock());
        // Modifying the parameter list should not affect the tile's blocks.
        assertEquals("Modifying list modified tile's blocks.",
            3, tile.getBlocks().size());

        tile = new Tile(blocks); // Testing 3 ground blocks, 1 non-ground.

        blocks = makeBlockList(GrassBlock.class, 4);
        try {
            tile = new Tile(blocks);
            fail("4 grass (ground) blocks, didn't throw.");
        } catch (TooHighException e) {} // Silence expected exceptions.

        blocks = makeBlockList(GrassBlock.class, 3);
        for (int i = 0; i < 5; i++) {
            blocks.add(new WoodBlock());
        }
        tile = new Tile(blocks); // Tests 3 ground blocks and 5 non-ground.

        blocks.add(new WoodBlock());
        try {
            tile = new Tile(blocks);
            fail("3 grass blocks, 6 wood blocks, didn't throw.");
        } catch (TooHighException e) {}
    }

    @Test
    public void testExits() throws Exception {
        Tile tile = new Tile();

        try {
            tile.addExit(null, tile);
            fail("Null exit name should throw but didn't.");
        } catch (NoExitException e) {}

        try {
            tile.addExit("exit name", null);
            fail("Null target tile should throw but didn't.");
        } catch (NoExitException e) {}

        Tile other = new Tile();
        tile.addExit("name", other);
        assertTrue(
            "New exit not stored.", tile.getExits().containsKey("name"));
        assertEquals(
            "New exit is the wrong tile.", other, tile.getExits().get("name"));

        Tile newTarget = new Tile();
        tile.addExit("name", newTarget);
        assertEquals(
            "Target with same name not overwritten.",
            newTarget, tile.getExits().get("name"));
    }

    // getBlocks is effectively tested in many other places, for example,
    // the constructor tests.

    @Test
    public void testGetExits() throws NoExitException {
        Tile tile = new Tile();
        tile.addExit("up", tile);
        tile.addExit("down", tile);

        Map<String, Tile> map = new HashMap<>();
        map.put("down", tile);
        map.put("up", tile);

        assertEquals("Incorrect exits.", map, tile.getExits());
    }

    @Test
    public void testGetTopBlock() throws Exception {
        Tile tile = new Tile(new ArrayList<Block>());

        try {
            tile.getTopBlock();
            fail("Get top block of empty tile didn't throw.");
        } catch (TooLowException e) {}

        Block top = new WoodBlock();
        tile = new Tile(Arrays.asList((Block)new SoilBlock(), top));
        assertEquals("Incorrect top block.", top, tile.getTopBlock());
    }

    @Test
    public void testRemoveTopBlock() throws Exception {
        Tile tile = new Tile(new ArrayList<Block>());

        try {
            tile.removeTopBlock();
            fail("Remove top block of empty tile didn't throw.");
        } catch (TooLowException e) {}

        List<Block> blockList = makeBlockList(WoodBlock.class, 2);

        Block bottomBlock = blockList.get(0);
        tile = new Tile(blockList);
        tile.removeTopBlock();
        assertEquals(
            "Top block incorrectly removed.",
            Arrays.asList(bottomBlock), tile.getBlocks()
        );
    }

    @Test
    public void testRemoveExit() throws Exception {
        Tile tile = new Tile();
        tile.addExit("up", tile);
        tile.addExit("right", tile);

        try {
            tile.removeExit(null);
            fail("Removing null exit didn't throw.");
        } catch (NoExitException e) {}

        try {
            tile.removeExit("down");
            fail("Removing non-existent exit didn't throw.");
        } catch (NoExitException e) {}

        Map<String, Tile> expected = new HashMap<String,Tile>();
        expected.put("right", tile);
        tile.removeExit("up");
        assertEquals(
            "Exit not removed correctly.", expected, tile.getExits());
    }

    @Test
    public void testDig() throws Exception {
        Tile tile = new Tile(new ArrayList<Block>());
        try {
            tile.dig();
            fail("Digging empty tile didn't throw.");
        } catch (TooLowException e) {}

        tile = new Tile();
        tile.placeBlock(new StoneBlock());
        try {
            tile.dig();
            fail("Digging undiggable block didn't throw.");
        } catch (InvalidBlockException e) {}

        List<Block> blocks = makeBlockList(WoodBlock.class, 5);
        tile = new Tile(blocks);
        Block topBlock = blocks.get(4);
        Block dugBlock = tile.dig();

        assertEquals(
            "Digging removed incorrect block.", topBlock, dugBlock);
        assertEquals(
            "Digging resulted in the incorrect blocks.",
            blocks.subList(0, 4), tile.getBlocks());
    }

    @Test
    public void testPlaceBlock() throws Exception {
        Tile tile = new Tile();
        try {
            tile.placeBlock(null);
            fail("Placing null block didn't throw.");
        } catch (InvalidBlockException e) {}
        try {
            tile.placeBlock(new SoilBlock());
            fail("Placing ground block above height 3 didn't throw.");
        } catch (TooHighException e) {}

        for (int i = 0; i < 5; i++) {
            Block topBlock = new WoodBlock();
            // Testing place until 8 blocks are on the tile. Should succeed.
            tile.placeBlock(topBlock);
            assertEquals("Block not placed on top.",
                topBlock, tile.getTopBlock());
        }
        try {
            tile.placeBlock(new WoodBlock());
            fail("Placing normal block above 8 didn't throw.");
        } catch (TooHighException e) {}
        try {
            tile.placeBlock(new SoilBlock());
            fail("Placing ground block above 8 didn't throw.");
        } catch (TooHighException e) {}
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
        // otherTile is 1 height lower here. Should succeed.
        tile.moveBlock("test exit");

        assertNotEquals(
            "Block not removed from original tile.",
            blockToMove, tile.getTopBlock());
        assertEquals(
            "Block placed on new tile.",
            blockToMove, otherTile.getTopBlock());

        otherTile.placeBlock(new StoneBlock());
        try {
            tile.moveBlock("test exit");
            fail("Moving to equal height tile didn't throw.");
        } catch (TooHighException e) {}

        // Specific test for empty tiles.
        tile = new Tile(new ArrayList<Block>());
        otherTile = new Tile(new ArrayList<Block>());
        tile.addExit("test 2", otherTile);
        try {
            tile.moveBlock("test 2");
            fail("Moving from empty tile to empty tile didn't throw.");
        } catch (TooHighException e) {}
    }
}