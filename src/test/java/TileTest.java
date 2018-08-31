import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * TileTest
 */
public class TileTest {
    Tile tile;
    Tile emptyTile;

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

    /**
     * Setup a single tile with the default starting blocks and another block
     * with no blocks.
     */
    @Before
    public void setupTile() {
        tile = new Tile();
        try {
            emptyTile = new Tile(new ArrayList<Block>());
        } catch (TooHighException e) {
            throw new AssertionError(e); // Will never happen...
        }
    }

    /** Tests the constructor with no arguments. */
    @Test
    public void testConstructor1() {
        List<Block> tileBlocks = tile.getBlocks();
        assertEquals(
            "Not 3 initial blocks.", 3, tileBlocks.size());
        // We can't use assertEquals() with a List<Block> because
        // .equals() for Block compares references, not types.
        assertTrue(
            "First block not soil.", tileBlocks.get(0) instanceof SoilBlock);
        assertTrue(
            "Second block not soil.", tileBlocks.get(1) instanceof SoilBlock);
        assertTrue(
            "Third block not grass.", tileBlocks.get(2) instanceof GrassBlock);
        assertEquals(
            "Initial exits exist.", 0, tile.getExits().size());
    }

    /** Tests constructor with a given block list. */
    @Test
    public void testConstructor3NormalBlocks() throws Exception {
        // Often in these test methods, there is code which could throw but
        // shouldn't. Such code is included bare in a test method which
        // throws Exception. Any exception outside a try/catch fails the test.

        List<Block> blocks = makeBlockList(GrassBlock.class, 3);

        tile = new Tile(blocks); // Testing 3 starting ground blocks.
        assertEquals("Incorrect blocks.", blocks, tile.getBlocks());

        blocks.add(new WoodBlock());
        // Modifying the parameter list should not affect the tile's blocks.
        assertEquals("Modifying list modified tile's blocks.",
                3, tile.getBlocks().size());

    }

    /** Constructor with 3 grass blocks and one wood. */
    @Test
    public void testConstructor4NormalBlocks() throws Exception {
        List<Block> blocks = makeBlockList(GrassBlock.class, 3);
        blocks.add(new WoodBlock());
        tile = new Tile(blocks);
    }

    /** Constructor with 4 grass blocks. Should throw. */
    @Test(expected = TooHighException.class)
    public void testConstructor4GroundBlocks() throws TooHighException {
        tile = new Tile(makeBlockList(GrassBlock.class, 4));
    }

    /** Constructor with 3 grass blocks, then 5 normal blocks. */
    @Test
    public void testConstructor8NormalBlocks() throws Exception {
        List<Block> blocks = makeBlockList(GrassBlock.class, 3);
        for (int i = 0; i < 5; i++) {
            blocks.add(new WoodBlock());
        }
        tile = new Tile(blocks); // Tests 3 ground blocks and 5 non-ground.
    }

    /** Constructor with 9 wood block. Should throw. */
    @Test(expected = TooHighException.class)
    public void testConstructor9Blocks() throws TooHighException {
        tile = new Tile(makeBlockList(WoodBlock.class, 9));
    }

    /** Adding exit with null name. Should throw. */
    @Test(expected = NoExitException.class)
    public void testNullExitName() throws NoExitException {
        tile.addExit(null, emptyTile);
    }

    /** Adding exit with null tile. Should throw. */
    @Test(expected = NoExitException.class)
    public void testNullExitTarget() throws NoExitException {
        tile.addExit("exit name", null);
    }

    /** Tests addExit works as expected and overwrites. */
    @Test
    public void testAddExitNormal() throws Exception {
        Tile other = new Tile();
        tile.addExit("name", other);
        assertTrue("New exit not stored.",
                tile.getExits().containsKey("name"));
        assertEquals("New exit is the wrong tile.",
                other, tile.getExits().get("name"));

        Tile newTarget = new Tile();
        tile.addExit("name", newTarget);
        assertEquals("Target with same name not overwritten.",
                newTarget, tile.getExits().get("name"));
    }

    // getBlocks is effectively tested in many other places, for example,
    // the constructor tests.

    /** Tests get exits with 2 exits. */
    @Test
    public void testGetExits() throws NoExitException {
        Tile tile = new Tile();
        tile.addExit("up", tile);
        tile.addExit("down", emptyTile);

        Map<String, Tile> map = new HashMap<>();
        map.put("down", tile);
        map.put("up", emptyTile);

        assertEquals("Incorrect exits.", map, tile.getExits());
    }

    /** Tests get top block with an empty tile throws. */
    @Test(expected = TooLowException.class)
    public void testGetTopBlockEmpty() throws TooLowException {
        emptyTile.getTopBlock();
    }

    /** Tests get top block returns the correct block. */
    @Test
    public void testGetTopBlockNormal() throws Exception {
        Block top = new WoodBlock();
        tile.placeBlock(top);
        assertEquals("Incorrect top block.", top, tile.getTopBlock());
    }

    /** Tests remove top block of empty tile throws. */
    @Test(expected = TooLowException.class)
    public void testRemoveTopBlockEmpty() throws TooLowException {
        emptyTile.removeTopBlock();
    }

    /** Tests remove top block works correctly. */
    @Test
    public void testRemoveTopBlockNormal() throws Exception {
        List<Block> blockList = makeBlockList(WoodBlock.class, 2);
        Block bottomBlock = blockList.get(0);
        tile = new Tile(blockList);
        tile.removeTopBlock();
        assertEquals("Top block incorrectly removed.",
                Arrays.asList(bottomBlock), tile.getBlocks());
    }

    /** Removing exit with null name should throw. */
    @Test(expected = NoExitException.class)
    public void testRemoveExitNullName() throws NoExitException {
        tile.removeExit(null);
    }

    /** Removing non-existent exit should throw. */
    @Test(expected = NoExitException.class)
    public void testRemoveExitNonExistent() throws NoExitException {
        tile.removeExit("doesn't exist");
    }

    /** Removing exit normally. */
    @Test
    public void testRemoveExitNormal() throws Exception {
        tile.addExit("up", tile);
        tile.addExit("right", tile);

        Map<String, Tile> expected = new HashMap<String,Tile>();
        expected.put("right", tile);
        tile.removeExit("up");
        assertEquals("Exit not removed correctly.", expected, tile.getExits());
    }

    /** Digging empty tile should throw. */
    @Test(expected = TooLowException.class)
    public void testDigNoBlocks() throws Exception {
        emptyTile.dig();
    }

    /** Digging undiggable tile should throw. */
    @Test(expected = InvalidBlockException.class)
    public void testDigUndiggable() throws Exception {
        tile.placeBlock(new StoneBlock());
        tile.dig();
    }

    /** Digging normally. */
    @Test
    public void testDigNormal() throws Exception {
        List<Block> blocks = makeBlockList(WoodBlock.class, 5);
        tile = new Tile(blocks);
        Block topBlock = blocks.get(4);
        Block dugBlock = tile.dig();

        assertEquals("Digging removed incorrect block.", topBlock, dugBlock);
        assertEquals("Digging resulted in the incorrect blocks.",
                blocks.subList(0, 4), tile.getBlocks());
    }

    /** Placing a null block should throw. */
    @Test(expected = InvalidBlockException.class)
    public void testPlaceBlockNull() throws Exception {
        tile.placeBlock(null);
    }

    /** Placing a ground block above 3 should throw. */
    @Test(expected = TooHighException.class)
    public void testPlaceGroundAbove3() throws Exception {
        tile.placeBlock(new SoilBlock());
    }

    /** Placing non ground blocks up to and including 8 blocks. */
    @Test
    public void testPlaceBlockNormal() throws Exception {
        for (int i = 0; i < 5; i++) {
            Block topBlock = new WoodBlock();
            // Testing place until 8 blocks are on the tile. Should succeed.
            tile.placeBlock(topBlock);
            assertEquals("Block not placed on top.",
                topBlock, tile.getTopBlock());
        }
    }

    /** Placing block when 8 blocks are on the tile. Should throw. */
    @Test(expected = TooHighException.class)
    public void testPlaceBlockAbove8() throws Exception {
        tile = new Tile(makeBlockList(WoodBlock.class, 8));
        tile.placeBlock(new WoodBlock());
    }

    /** Moving via non-existent exit should throw. */
    @Test(expected = NoExitException.class)
    public void testMoveBlockNoExit() throws Exception {
        tile.moveBlock("non-existent");
    }

    /** Moving via null exit should throw. */
    @Test(expected = NoExitException.class)
    public void testMoveBlockNullExit() throws Exception {
        tile.moveBlock(null);
    }

    /** Moving an unmoveable block should throw. */
    @Test(expected = InvalidBlockException.class)
    public void testMoveBlockUnmoveable() throws Exception {
        tile.addExit("exit name", new Tile());
        tile.placeBlock(new StoneBlock());
        tile.moveBlock("exit name");
    }

    /** Moving block to an adjacent tile of same height should throw. */
    @Test(expected = TooHighException.class)
    public void testMoveBlockSameHeight() throws Exception {
        Tile otherTile = new Tile();
        tile.addExit("test exit", otherTile);
        tile.moveBlock("test exit");
    }

    /** Moving block to tile one lower should succeed. */
    @Test
    public void testMoveBlockToOneLower() throws Exception {
        Tile otherTile = new Tile();

        Block blockToMove = new WoodBlock();
        tile.placeBlock(blockToMove);
        // otherTile is 1 height lower here. Should succeed.
        tile.addExit("test exit", otherTile);
        tile.moveBlock("test exit");

        assertNotEquals("Block not removed from original tile.",
                blockToMove, tile.getTopBlock());
        assertEquals("Block placed on new tile.",
                blockToMove, otherTile.getTopBlock());
    }

    /** Moving from empty to empty tile should throw TooHigh. */
    @Test(expected = TooHighException.class)
    public void testMoveBlockEmptyTiles() throws Exception {
        Tile otherEmptyTile = new Tile(new ArrayList<Block>());
        emptyTile.addExit("test 2", otherEmptyTile);
        emptyTile.moveBlock("test 2");
    }
}