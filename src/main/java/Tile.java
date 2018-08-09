/**
 * Tiles for a map. Contains Blocks Maintains a mapping between exit names and
 * other tiles.
 *
 */
public class Tile implements java.io.Serializable {

    /**
     * Construct a new tile. Each tile should be constructed with no exits
     * (getExits().size() == 0). Each tile must be constructed to start with two
     * soil blocks and then a grass block on top. i.e. getBlocks() must contain
     * {SoilBlock, SoilBlock, GrassBlock} for a new Tile.
     *
     */
    public Tile() {
    }

    /**
     * Construct a new tile. Each tile should be constructed with no exits
     * (getExits().size() == 0). Set the blocks on the tile to be the contents of
     * startingBlocks. Index 0 in startingBlocks is the lowest block on the tile,
     * while index N -1 is the top block on the tile for N blocks. startingBlocks
     * cannot be null. i.e. getBlocks() must contain the contents of startingBlocks,
     * but modifying startingBlocks after constructing the Tile should not change
     * the results of getBlocks(). Handle the following cases:
     * 
     * If startingBlocks contains more than 8 elements, throw a TooHighException. If
     * startingBlocks contains an instance of GroundBlock that is at an index of 3
     * or higher, throw a TooHighException.
     * 
     * @param startingBlocks a list of blocks on the tile, cannot be null
     * @throws TooHighException if startingBlocks.size() > 8, or if startingBlocks
     *                          elements ≥ 3 are instances of GroundBlock
     */
    public Tile(java.util.List<Block> startingBlocks) throws TooHighException {
    }

    /**
     * What exits are there from this Tile? No ordering is required.
     * 
     * @return map of names to Tiles
     */
    public java.util.Map<java.lang.String, Tile> getExits() {
    }

    /**
     * What Blocks are on this Tile? Order of blocks returned must be in order of
     * height. Index 0 is bottom, and index N - 1 is the top, for N blocks.
     * 
     * @return Blocks on the Tile
     */
    public java.util.List<Block> getBlocks() {
    }

    /**
     * Return the block that is the top block on the tile. If there are no blocks,
     * throw a TooLowException
     * 
     * @throws TooLowException if there are no blocks on the tile
     * @return top Block or null if no blocks
     */
    public Block getTopBlock() throws TooLowException {
    }

    /**
     * Remove the block on top of the tile Throw a TooLowException if there are no
     * blocks on the tile
     * 
     * @throws TooLowException if there are no blocks on the tile
     */
    public void removeTopBlock() throws TooLowException {
    }

    /**
     * Add a new exit to this tile The Map returned by getExits() must now include
     * an entry (name, target). Overwrites any existing exit with the same name If
     * name or target is null, throw a NoExitException
     * 
     * @param name   Name of the exit
     * @param target Tile the exit goes to
     * @throws NoExitException if name or target is null
     */
    public void addExit(java.lang.String name, Tile target) throws NoExitException {
    }

    /**
     * Remove an exit from this tile The Map returned by getExits() must no longer
     * have the key name. If name does not exist in getExits(), or name is null,
     * throw a NoExitException.
     * 
     * @param name Name of exit to remove
     * @throws NoExitException if name is not in exits, or name is null
     */
    public void removeExit(java.lang.String name) throws NoExitException {
    }

    /**
     * Attempt to dig in the current tile. If the top block (given by getTopBlock())
     * is diggable (block.isDiggable()), remove the top block of the tile and return
     * it. Handle the following cases:
     * 
     * Throw a TooLowException if there are no blocks on the tile Throw an
     * InvalidBlockException if the block is not diggable
     * 
     * @throws TooLowException       if there are no blocks on the tile
     * @throws InvalidBlockException if the block is not diggable
     * @return the removed block or null
     */
    public Block dig() throws TooLowException, InvalidBlockException {
    }

    /**
     * Attempt to move the current top block to another tile. Remove the top block
     * (given by getTopBlock()) from this tile and add it to the tile at the named
     * exit (getExits(exitName)), if the block is moveable (block.isMoveable()) and
     * the height of that tile (the number of blocks given by getBlocks().size()) is
     * less than the current tile *before* the move. Handle the following cases:
     * 
     * If the exit is null, or does not exist, throw a NoExitException If the number
     * of blocks on the target tile is ≥ to this one, throw a TooHighException If
     * the block is not moveable, throw a InvalidBlockException
     * 
     * @param exitName the name of the exit to move the block to
     * @throws TooHighException      if the target tile is ≥ to this one.
     * @throws InvalidBlockException if the block is not moveable
     * @throws NoExitException       if the exit is null or does not exist
     */
    public void moveBlock(java.lang.String exitName) throws TooHighException, InvalidBlockException, NoExitException {
    }

    /**
     * Place a block on a tile. Add the block to the top of the blocks on this tile.
     * If the block is an instance of GroundBlock, it can only be placed
     * underground. Handle the following cases:
     * 
     * If the block is null, throw an InvalidBlockException If the target block has
     * more than 8 blocks already, or if the block is a GroundBlock and the target
     * block has more than 3 blocks already, throw a TooHighException
     * 
     * @param block the block to place.
     * @throws TooHighException      if there are already 8 blocks on the tile, or
     *                               if this is a ground block and there are already
     *                               3 blocks on the tile.
     * @throws InvalidBlockException if the block is null
     */
    public void placeBlock(Block block) throws TooHighException, InvalidBlockException {
    }

}