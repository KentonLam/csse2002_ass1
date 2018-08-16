import org.junit.Assert;

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
    /** Helper method to generate a list of a certain type of block. */
    private <T extends Block> List<Block> makeBlockList(
            Class<T> classType, int size) {

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

    @Test
    public void testConstructor1() {
        Tile t = new Tile();
        List<Block> b = t.getBlocks();

        Assert.assertEquals(
            "Not 3 initial blocks.", 3, b.size());
        // We can't use Assert.assertEquals() with a List<Block> because .equals()
        // for Block compares references, not types.
        Assert.assertTrue(
            "First block not soil.", b.get(0) instanceof SoilBlock);
        Assert.assertTrue(
            "Second block not soil.", b.get(1) instanceof SoilBlock);
        Assert.assertTrue(
            "Third block not grass.", b.get(2) instanceof GrassBlock);
        Assert.assertEquals(
            "Initial exits exist.", 0, t.getExits().size());
    }

    @Test
    public void testConstructor2() throws Exception {
        // Often in these test methods, there is code which could throw but
        // shouldn't. Such code is included bare in a test method which
        // throws Exception. Any exception outside a try/catch fails the test.

        List<Block> blocks = makeBlockList(GrassBlock.class, 3);

        Tile t = new Tile(blocks); // Testing 3 starting ground blocks.
        Assert.assertEquals("Incorrect blocks.", blocks, t.getBlocks());

        blocks.add(new WoodBlock());
        // Modifying the parameter list should not affect the tile's blocks.
        Assert.assertEquals("Modifying list modified tile's blocks.",
            3, t.getBlocks().size());

        blocks.add(new WoodBlock());
        t = new Tile(blocks); // Testing 3 ground blocks, 1 non-ground.

        blocks.remove(blocks.size()-1);
        blocks.add(new GrassBlock());
        try {
            t = new Tile(blocks);
            Assert.fail("4 grass (ground) blocks, didn't throw.");
        } catch (TooHighException e) {}

        blocks.remove(blocks.size()-1);
        for (int i = 0; i < 5; i++) {
            blocks.add(new WoodBlock());
        }
        t = new Tile(blocks); // Tests 3 ground blocks and 5 non-ground.

        blocks.add(new WoodBlock());
        try {
            t = new Tile(blocks);
            Assert.fail("3 grass blocks, 6 wood blocks, didn't throw.");
        } catch (TooHighException e) {}
    }

    @Test
    public void testExits() throws Exception {
        Tile t = new Tile();

        try {
            t.addExit(null, t);
            Assert.fail("Null exit name should throw but didn't.");
        } catch (NoExitException e) {}

        try {
            t.addExit("exit name", null);
            Assert.fail("Null target tile should throw but didn't.");
        } catch (NoExitException e) {}

        Tile other = new Tile();
        t.addExit("name", other);
        Assert.assertTrue(
            "New exit not stored.", t.getExits().containsKey("name"));
        Assert.assertEquals(
            "New exit is the wrong tile.", other, t.getExits().get("name"));

        Tile newTarget = new Tile();
        t.addExit("name", newTarget);
        Assert.assertEquals(
            "Target with same name not overwritten.",
            newTarget, t.getExits().get("name"));
    }

    // getBlocks is effectively tested in many other places, for example,
    // the constructor tests.

    @Test
    public void testGetExits() throws NoExitException {
        Tile t = new Tile();
        t.addExit("up", t);
        t.addExit("down", t);

        Map<String, Tile> m = new HashMap<>();
        m.put("down", t);
        m.put("up", t);

        Assert.assertEquals("Incorrect exits.", m, t.getExits());
    }

    @Test
    public void testGetTopBlock() throws Exception {
        Tile t = new Tile(new ArrayList<Block>());

        try {
            t.getTopBlock();
            Assert.fail("Get top block of empty tile didn't throw.");
        } catch (TooLowException e) {}

        Block top = new WoodBlock();
        t = new Tile(Arrays.asList((Block)new SoilBlock(), top));
        Assert.assertEquals("Incorrect top block.", top, t.getTopBlock());
    }

    @Test
    public void testRemoveTopBlock() throws Exception {
        Tile t = new Tile(new ArrayList<Block>());

        try {
            t.removeTopBlock();
            Assert.fail("Remove top block of empty tile didn't throw.");
        } catch (TooLowException e) {}

        List<Block> blockList = makeBlockList(WoodBlock.class, 2);

        Block bottomBlock = blockList.get(0);
        t = new Tile(blockList);
        t.removeTopBlock();
        Assert.assertEquals(
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
            Assert.fail("Removing null exit didn't throw.");
        } catch (NoExitException e) {}

        try {
            t.removeExit("down");
            Assert.fail("Removing non-existent exit didn't throw.");
        } catch (NoExitException e) {}

        Map<String, Tile> expected = new HashMap<String,Tile>();
        expected.put("right", t);
        t.removeExit("up");
        Assert.assertEquals(
            "Exit not removed correctly.", expected, t.getExits());
    }

    @Test
    public void testDig() throws Exception {
        Tile t = new Tile(new ArrayList<Block>());
        try {
            t.dig();
            Assert.fail("Digging empty tile didn't throw.");
        } catch (TooLowException e) {}

        t = new Tile();
        t.placeBlock(new StoneBlock());
        try {
            t.dig();
            Assert.fail("Digging undiggable block didn't throw.");
        } catch (InvalidBlockException e) {}

        List<Block> blocks = makeBlockList(WoodBlock.class, 5);
        t = new Tile(blocks);
        Block topBlock = blocks.get(4);
        Block dugBlock = t.dig();

        Assert.assertEquals(
            "Digging removed incorrect block.", topBlock, dugBlock);
        Assert.assertEquals(
            "Digging resulted in the incorrect blocks.",
            blocks.subList(0, 4), t.getBlocks());
    }

    @Test
    public void testPlaceBlock() throws Exception {
        Tile t = new Tile();
        try {
            t.placeBlock(null);
            Assert.fail("Placing null block didn't throw.");
        } catch (InvalidBlockException e) {}
        try {
            t.placeBlock(new SoilBlock());
            Assert.fail("Placing ground block above height 3 didn't throw.");
        } catch (TooHighException e) {}

        for (int i = 0; i < 5; i++) {
            Block topBlock = new WoodBlock();
            // Testing place until 8 blocks are on the tile. Should succeed.
            t.placeBlock(topBlock);
            Assert.assertEquals("Block not placed on top.",
                topBlock, t.getTopBlock());
        }
        try {
            t.placeBlock(new WoodBlock());
            Assert.fail("Placing normal block above 8 didn't throw.");
        } catch (TooHighException e) {}
        try {
            t.placeBlock(new SoilBlock());
            Assert.fail("Placing ground block above 8 didn't throw.");
        } catch (TooHighException e) {}
    }

    @Test
    public void testMoveBlock() throws Exception {
        Tile tile = new Tile();
        Tile otherTile = new Tile();

        try {
            tile.moveBlock("non-existent");
            Assert.fail("Moving via non-existent exit didn't throw.");
        } catch (NoExitException e) {}

        try {
            tile.moveBlock(null);
            Assert.fail("Moving via null exit didn't throw.");
        } catch (NoExitException e) {}

        tile.addExit("test exit", otherTile);

        tile.placeBlock(new StoneBlock());
        try {
            tile.moveBlock("test exit");
            Assert.fail("Moving unmoveable block didn't throw.");
        } catch (InvalidBlockException e) {}

        Block blockToMove = new WoodBlock();
        tile.placeBlock(blockToMove);
        otherTile.placeBlock(new StoneBlock());
        // otherTile is 1 height lower here. Should succeed.
        tile.moveBlock("test exit");

        Assert.assertNotEquals(
            "Block not removed from original tile.",
            blockToMove, tile.getTopBlock());
        Assert.assertEquals(
            "Block placed on new tile.",
            blockToMove, otherTile.getTopBlock());

        otherTile.placeBlock(new StoneBlock());
        try {
            tile.moveBlock("test exit");
            Assert.fail("Moving to equal height tile didn't throw.");
        } catch (TooHighException e) {}

        // Specific test for empty tiles.
        tile = new Tile(new ArrayList<Block>());
        otherTile = new Tile(new ArrayList<Block>());
        tile.addExit("test 2", otherTile);
        try {
            tile.moveBlock("test 2");
            Assert.fail("Moving from empty tile to empty tile didn't throw.");
        } catch (TooHighException e) {}
    }
}