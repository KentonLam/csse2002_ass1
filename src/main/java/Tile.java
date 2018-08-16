import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tiles for a map, representing a vertical stack of blocks.
 * Maintains a mapping to other tiles via certain exits.
 */
public class Tile implements java.io.Serializable {

    private final List<Block> blocks = new ArrayList<Block>();
    private final Map<String, Tile> exits = new HashMap<String, Tile>();
    private static final int MAX_BLOCK_HEIGHT = 8;
    private static final int MAX_GROUND_HEIGHT = 3;

    /**
     * Construct a new tile.<br/>
     * Each tile should be constructed with no exits (getExits().size() == 0). <br/>
     * Each tile must be constructed to start with two soil blocks and then a grass
     * block on top.<br/>
     * i.e. getBlocks() must contain {SoilBlock, SoilBlock, GrassBlock} for a new
     * Tile.
     *
     */
    public Tile() {
        // TODO Is there a neater way to do this in java? :/
        this.blocks.add(new SoilBlock());
        this.blocks.add(new SoilBlock());
        this.blocks.add(new GrassBlock());
    }

    /**
     * Construct a new tile.<br/>
     * Each tile should be constructed with no exits (getExits().size() == 0). <br/>
     * Set the blocks on the tile to be the contents of startingBlocks. <br/>
     * Index 0 in startingBlocks is the lowest block on the tile, while index N -1
     * is the top block on the tile for N blocks. <br/>
     * startingBlocks cannot be null. <br/>
     * i.e. getBlocks() must contain the contents of startingBlocks, but modifying
     * startingBlocks after constructing the Tile should not change the results of
     * getBlocks(). <br/>
     * Handle the following cases:
     * <ol>
     * <li>If startingBlocks contains more than 8 elements, throw a
     * TooHighException.</li>
     * <li>If startingBlocks contains an instance of GroundBlock that is at an index
     * of 3 or higher, throw a TooHighException.</li>
     * </ol>
     *
     * @param startingBlocks a list of blocks on the tile, cannot be null
     * @throws TooHighException if startingBlocks.size() > 8, or if startingBlocks
     *                          elements ≥ 3 are instances of GroundBlock
     */
    public Tile(java.util.List<Block> startingBlocks) throws TooHighException {
        for (Block b : startingBlocks) {
            try {
                // We can do this because if the constructor throws, the
                // instance doesn't get created, so no half constructed tile
                // will ever be created.
                this.placeBlock(b);
            } catch (InvalidBlockException e) {
                // By assumption, the elements of startingBlocks are non-null.
                // Only here because this constructor can't throw invalid block.
                throw new AssertionError(e);
            }
        }
    }

    /**
     * What exits are there from this Tile? <br/>
     * No ordering is required.
     *
     * @return map of names to Tiles
     */
    public java.util.Map<String, Tile> getExits() {
        return this.exits;
    }

    /**
     * What Blocks are on this Tile? <br/>
     * Order of blocks returned must be in order of height. <br/>
     * Index 0 is bottom, and index N - 1 is the top, for N blocks.
     *
     * @return Blocks on the Tile
     */
    public java.util.List<Block> getBlocks() {
        return this.blocks;
    }

    private void ensureNonEmpty() throws TooLowException {
        if (this.blocks.size() <= 0)
            throw new TooLowException();
    }

    /**
     * Return the block that is the top block on the tile. <br/>
     * If there are no blocks, throw a TooLowException
     *
     * @throws TooLowException if there are no blocks on the tile
     * @return top Block or null if no blocks
     */
    public Block getTopBlock() throws TooLowException {
        this.ensureNonEmpty();
        return this.blocks.get(this.blocks.size()-1);
    }

    /**
     * Remove the block on top of the tile <br/>
     * Throw a TooLowException if there are no blocks on the tile <br/>
     *
     * @throws TooLowException if there are no blocks on the tile
     */
    public void removeTopBlock() throws TooLowException {
        this.ensureNonEmpty();
        this.blocks.remove(this.blocks.size()-1);
    }

    /**
     * Add a new exit to this tile <br/>
     * The Map returned by getExits() must now include an entry (name, target).
     * Overwrites any existing exit with the same name <br/>
     * If name or target is null, throw a NoExitException
     *
     * @param name   Name of the exit
     * @param target Tile the exit goes to
     * @throws NoExitException if name or target is null
     */
    public void addExit(String name, Tile target) throws NoExitException {
        if (name == null || target == null)
            throw new NoExitException();
        this.exits.put(name, target);
    }

    /**
     * Remove an exit from this tile <br/>
     * The Map returned by getExits() must no longer have the key name. <br/>
     * If name does not exist in getExits(), or name is null, throw a
     * NoExitException.
     *
     * @param name Name of exit to remove
     * @throws NoExitException if name is not in exits, or name is null
     */
    public void removeExit(String name) throws NoExitException {
        this.ensureCanExit(name);
        this.exits.remove(name);
    }

    private void ensureCanExit(String exitName) throws NoExitException {
        if (exitName == null || !this.exits.containsKey(exitName)
                || this.exits.get(exitName) == null)
            throw new NoExitException();
    }

    /**
     * Attempt to dig in the current tile. <br/>
     * If the top block (given by getTopBlock()) is diggable (block.isDiggable()),
     * remove the top block of the tile and return it. <br/>
     * Handle the following cases:
     * <ol>
     * <li>Throw a TooLowException if there are no blocks on the tile</li>
     * <li>Throw an InvalidBlockException if the block is not diggable</li>
     * </ol>
     *
     * @throws TooLowException       if there are no blocks on the tile
     * @throws InvalidBlockException if the block is not diggable
     * @return the removed block or null
     */
    public Block dig() throws TooLowException, InvalidBlockException {
        this.ensureNonEmpty();
        Block topBlock = this.getTopBlock();
        if (!topBlock.isDiggable())
            throw new InvalidBlockException();
        this.removeTopBlock();
        return topBlock;
    }

    /**
     * Attempt to move the current top block to another tile. Remove the top block
     * (given by getTopBlock()) from this tile and add it to the tile at the named
     * exit (getExits(exitName)), if the block is moveable (block.isMoveable()) and
     * the height of that tile (the number of blocks given by getBlocks().size()) is
     * less than the current tile *before* the move. <br/>
     * Handle the following cases:
     * <ul>
     * <li>If the exit is null, or does not exist, throw a NoExitException</li>
     * <li>If the number of blocks on the target tile is ≥ to this one, throw a
     * TooHighException</li>
     * <li>If the block is not moveable, throw a InvalidBlockException</li>
     * </ul>
     *
     * @param exitName the name of the exit to move the block to
     * @throws TooHighException      if the target tile is ≥ to this one.
     * @throws InvalidBlockException if the block is not moveable
     * @throws NoExitException       if the exit is null or does not exist
     */
    public void moveBlock(String exitName)
            throws TooHighException, InvalidBlockException, NoExitException {
        this.ensureCanExit(exitName);

        Tile newTile = this.exits.get(exitName);
        if (newTile.getBlocks().size() >= this.blocks.size())
            throw new TooHighException();

        try {
            if (!this.getTopBlock().isMoveable())
                throw new InvalidBlockException();
        } catch (TooLowException e) {
            // This should never happen because if our height is 0, it is
            // necessarily <= any other tile's height and we would have thrown
            // TooHighException already.
            throw new AssertionError(e);
        }

        // The block can be moved, move it.
        Block b = this.blocks.remove(this.blocks.size()-1);
        try {
            newTile.placeBlock(b);
        } catch (TooHighException e) {
            // Similarly to TooLowException in getTopBlock() being impossible,
            // this will never throw a TooHighException. Because the other height
            // must be < our height, it will always be valid for our top block.
            throw new AssertionError(e);
        }
    }

    /**
     * Place a block on a tile. Add the block to the top of the blocks on this tile.
     * If the block is an instance of GroundBlock, it can only be placed
     * underground. Handle the following cases:
     * <ul>
     * <li>If the block is null, throw an InvalidBlockException</li>
     * <li>If the target block has more than 8 blocks already, or if the block is a
     * GroundBlock and the target block has more than 3 blocks already, throw a
     * TooHighException</li>
     * </ul>
     *
     * @param block the block to place.
     * @throws TooHighException      if there are already 8 blocks on the tile, or
     *                               if this is a ground block and there are already
     *                               3 blocks on the tile.
     * @throws InvalidBlockException if the block is null
     */
    public void placeBlock(Block block)
            throws TooHighException, InvalidBlockException {

        if (block == null)
            throw new InvalidBlockException();
        int maxHeight = (block instanceof GroundBlock
            ? MAX_GROUND_HEIGHT : MAX_BLOCK_HEIGHT);
        if (this.blocks.size() >= maxHeight)
            throw new TooHighException();
        this.blocks.add(block);
    }

}