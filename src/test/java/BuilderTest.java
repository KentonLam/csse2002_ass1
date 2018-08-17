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
        inventory.add((Block)new SoilBlock());
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
}
