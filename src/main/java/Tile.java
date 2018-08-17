import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tiles for a map, representing a vertical stack of blocks.
 * Maintains a mapping to other tiles via certain exits.
 */
public class Tile implements java.io.Serializable {

    /**
     * Blocks on the tile, first index is bottom-most. Marked final so
     * reference does not change.
     */
    private final List<Block> blocks = new ArrayList<Block>();
    /** Exit mappings, from name to another tile. */
    private final Map<String, Tile> exits = new HashMap<String, Tile>();
    /** Maximum height of normal blocks on a tile. */
    private static final int MAX_BLOCK_HEIGHT = 8;
    /** Maximum height of ground blocks on a tile. */
    private static final int MAX_GROUND_HEIGHT = 3;

    /**
     * Constructs a new tile with no exits, and with two soil blocks and then
     * a grass block.
     */
    public Tile() {
        this.blocks.add(new SoilBlock());
        this.blocks.add(new SoilBlock());
        this.blocks.add(new GrassBlock());
    }

    /**
     * Constructs a new tile with no exits, and the starting blocks as
     * specified.
     *
     * <p> Handles the following cases:
     * <ul>
     * <li> If startingBlocks contains more than 8 elements, throws a
     *      TooHighException. </li>
     * <li> If startingBlocks contains an instance of GroundBlock that is at
     *      an index of 3 or higher, also throws a TooHighException. </li>
     * </ul>
     *
     * @param startingBlocks a list of blocks, cannot be null.
     * @throws TooHighException if startingBlocks.size() > 8, or if
     *                          elements ≥ 3 are instances of GroundBlock.
     */
    public Tile(java.util.List<Block> startingBlocks) throws TooHighException {
        for (Block b : startingBlocks) {
            try {
                // We can do this because if the constructor throws, the
                // instance doesn't get created, so no half constructed tile
                // will ever be returned.
                this.placeBlock(b);
            } catch (InvalidBlockException e) {
                // By assumption, the elements of startingBlocks are non-null.
                // Only here because this constructor can't throw invalid block.
                throw new AssertionError(e);
            }
        }
    }

    /**
     * Possible exits from this tile.
     *
     * @return mapping from exit directions to tiles, unordered.
     */
    public java.util.Map<String, Tile> getExits() {
        return this.exits;
    }

    /**
     * Blocks currently on this tile, ordered with the first element being
     * the bottom-most block.
     *
     * @return blocks on the tile.
     */
    public java.util.List<Block> getBlocks() {
        return this.blocks;
    }

    /**
     * Helper method to throw TooLowException if there are no blocks on the
     * tile.
     */
    private void ensureNonEmpty() throws TooLowException {
        if (this.blocks.size() <= 0) {
            throw new TooLowException();
        }
    }

    /**
     * Return the top block of this tile.
     * (i.e. the last element of getBlocks().)
     *
     * @throws TooLowException if there are no blocks on the tile.
     * @return the top block.
     */
    public Block getTopBlock() throws TooLowException {
        this.ensureNonEmpty();
        return this.blocks.get(this.blocks.size()-1);
    }

    /**
     * Remove the top block on this tile.
     *
     * @throws TooLowException if there are no blocks on the tile.
     */
    public void removeTopBlock() throws TooLowException {
        this.ensureNonEmpty();
        this.blocks.remove(this.blocks.size()-1);
    }

    /**
     * Adds a new exit to this tile, with the given direction name and tile
     * target. Overwrites any existing exit with the same name.
     *
     * @param name   name of the exit.
     * @param target tile the exit goes to.
     * @throws NoExitException if name or target is null.
     */
    public void addExit(String name, Tile target) throws NoExitException {
        if (name == null || target == null) {
            throw new NoExitException();
        }
        this.exits.put(name, target);
    }

    /**
     * Removes an exit from this tile.
     *
     * @param name name of exit to remove.
     * @throws NoExitException if name is not in exits or name is null.
     */
    public void removeExit(String name) throws NoExitException {
        this.ensureCanExit(name);
        this.exits.remove(name);
    }

    /**
     * Helper method to throw if an exit a certain name does not exist or is
     * null.
     */
    private void ensureCanExit(String exitName) throws NoExitException {
        if (exitName == null || !this.exits.containsKey(exitName)
                || this.exits.get(exitName) == null) {
            throw new NoExitException();
        }
    }

    /**
     * Attempts to dig and return the top block of the tile.
     *
     * @throws TooLowException       if there are no blocks on the tile.
     * @throws InvalidBlockException if the top block is not diggable.
     * @return the removed block.
     */
    public Block dig() throws TooLowException, InvalidBlockException {
        this.ensureNonEmpty();
        Block topBlock = this.getTopBlock();
        if (!topBlock.isDiggable()) {
            throw new InvalidBlockException();
        }
        this.removeTopBlock();
        return topBlock;
    }

    /**
     * Moves our top block to another tile, via the given exit. The top block
     * must be moveable.
     *
     * <p> Removes the top block from this tile and places it on top of the
     * tile mapped to by exitName.
     *
     * @param exitName the name of the exit to move the block to.
     * @throws TooHighException      if the target's height is ≥ this height.
     * @throws InvalidBlockException if our top block is not moveable.
     * @throws NoExitException       if the exit is null or does not exist.
     */
    public void moveBlock(String exitName)
            throws TooHighException, InvalidBlockException, NoExitException {
        this.ensureCanExit(exitName);

        Tile newTile = this.exits.get(exitName);
        // If the new height is >= our height, moving the block will be
        // blocked by other blocks in the new tile, throw.
        if (newTile.getBlocks().size() >= this.blocks.size()) {
            throw new TooHighException();
        }

        try {
            if (!this.getTopBlock().isMoveable()) {
                throw new InvalidBlockException();
            }
        } catch (TooLowException e) {
            // This should never happen because if our height is 0, it is
            // necessarily <= any other tile's height and we would have thrown
            // TooHighException already.
            throw new AssertionError(e);
        }

        // If we reach here, the block can be moved, move it.
        Block b = this.blocks.remove(this.blocks.size()-1);
        try {
            newTile.placeBlock(b);
        } catch (TooHighException e) {
            // Similarly to TooLowException in getTopBlock() being impossible,
            // this will never throw a TooHighException. Because the new tile
            // must be < our height, it will always be valid for our top block.
            throw new AssertionError(e);
        }
    }

    /**
     * Places a block on this tile.
     *
     * <p> Normal blocks cannot be placed if there are already 8 or more
     * blocks on the tile. If the block is an instance of GroundBlock, it
     * <i>cannot</i> be placed if there are 3 or more blocks on the tile
     * already.
     *
     * @param block the block to place.
     * @throws TooHighException      if there are already 8 blocks on the tile,
     *                               or this is a ground block and there are
     *                               already 3 or more blocks on this tile.
     * @throws InvalidBlockException if the block is null.
     */
    public void placeBlock(Block block)
            throws TooHighException, InvalidBlockException {
        if (block == null) { // Checking the trivial case.
            throw new InvalidBlockException();
        }
        // Gets the max height appropriate for the block's type.
        // Could be generalised to place limits on arbitrary block types.
        int maxHeight = (block instanceof GroundBlock
            ? MAX_GROUND_HEIGHT : MAX_BLOCK_HEIGHT);
        if (this.blocks.size() >= maxHeight) {
            throw new TooHighException();
        }
        this.blocks.add(block); // The block is valid at this height, places.
    }

}