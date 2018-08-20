import org.junit.Assert;

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
        Assert.assertEquals("name", b.getName());
        Assert.assertEquals(t, b.getCurrentTile());
    }

    @Test
    public void testConstructor2() throws Exception {
        Tile t = new Tile();
        List<Block> l = new ArrayList<Block>();
        l.add(new StoneBlock());

        try {
            Builder b = new Builder("name", t, l);
            Assert.fail("Uncarryable starting block didn't throw.");
        } catch (InvalidBlockException e) {}

        l.clear();
        l.add(new WoodBlock());
        l.add(new WoodBlock());
        l.add(new WoodBlock());
        // Shouldn't throw with carryable block.
        Builder b = new Builder("name", t, l);

        Assert.assertEquals(
            "Incorrect starting inventory.", l, b.getInventory());
    }

    @Test
    public void testDropFromInventory() throws Exception {
        List<Block> inventory = new ArrayList<>();
        inventory.add(new SoilBlock());
        Block theBlock = new WoodBlock();
        inventory.add(theBlock);
        inventory.add(new WoodBlock());
        inventory.add(new WoodBlock());

        Tile t = new Tile();
        Builder b = new Builder("test name", t, inventory);
        try {
            b.dropFromInventory(-1);
            Assert.fail("Negative index didn't throw.");
        } catch (InvalidBlockException e) {}
        try {
            b.dropFromInventory(4);
            Assert.fail("Too high index didn't throw.");
        } catch (InvalidBlockException e) {}

        b.dropFromInventory(1);
        Assert.assertEquals("Wrong block placed.", theBlock, t.getTopBlock());
        Assert.assertFalse("Block still in inventory.",
            b.getInventory().contains(theBlock));
    }

    @Test
    public void testDigOnCurrentTile() throws Exception {
        Tile t = new Tile(new ArrayList<Block>());
        Builder b = new Builder("test", t);

        try {
            b.digOnCurrentTile();
            Assert.fail("Digging empty tile didn't throw.");
        } catch (TooLowException e) {}

        t.placeBlock(new SoilBlock());
        t.placeBlock(new StoneBlock());
        try {
            b.digOnCurrentTile();
            Assert.fail("Digging undiggable block didn't throw.");
        } catch (InvalidBlockException e) {}

        Block nonCarryable = new GrassBlock();
        t.placeBlock(nonCarryable);
        b.digOnCurrentTile();
        Assert.assertNotEquals("Block not removed from tile.",
            nonCarryable, t.getTopBlock());
        Assert.assertFalse("Uncarryable block placed in inventory.",
            b.getInventory().contains(nonCarryable));

        Block carryable = new WoodBlock();
        t.placeBlock(carryable);
        b.digOnCurrentTile();
        Assert.assertTrue("Carryable block not placed into inventory.",
            b.getInventory().contains(carryable));
    }

    @Test
    public void testCanEnter() throws Exception {
        Tile t = new Tile();
        Builder b = new Builder("test", t);

        Assert.assertFalse("Can enter null tile.", b.canEnter(null));

        Tile t2 = new Tile();
        Assert.assertFalse("Can enter unconnected tile.", b.canEnter(t2));

        t.placeBlock(new WoodBlock());
        t.addExit("exit name", t2);
        Assert.assertTrue("Cannot enter a connected tile one block lower.",
            b.canEnter(t2));

        t.placeBlock(new WoodBlock());
        Assert.assertFalse("Can enter a tile 2 blocks lower.",
            b.canEnter(t2));
    }
}
