import java.util.ArrayList;
import java.util.List;

/**
 * A player who can modify the map.
 *
 * Manages the player's inventory and position on the map.
 */
public class Builder {

    /** Player's current inventory. All blocks should be carryable. */
    private java.util.List<Block> inventory;
    /** Tile the player is currently on. */
    private Tile currentTile;
    /** Player's name. */
    private String name;

    /**
     * Creates a builder with a given name and starting on the given tile.
     *
     * @param name         name of the builder, cannot be null.
     * @param startingTile the tile the builder starts in, cannot be null.
     */
    public Builder(String name, Tile startingTile) {
        // Because the other constructor throws but this one doesn't, we
        // can't chain the other one from here.
        this.name = name;
        this.currentTile = startingTile;
        this.inventory = new ArrayList<Block>();
    }

    /**
     * Creates a builder with the specified name, starting on a given tile
     * and with a certain inventory.
     *
     * @param name              name of the builder, cannot be null.
     * @param startingTile      tile the builder starts in, cannot be null.
     * @param startingInventory starting inventory, cannot be null.
     * @throws InvalidBlockException if any block in startingInventory is
     *                               not carryable.
     */
    public Builder(String name, Tile startingTile,
            List<Block> startingInventory) throws InvalidBlockException {
        this(name, startingTile);
        for (Block b : startingInventory) {
            if (!b.isCarryable()) {
                throw new InvalidBlockException();
            }
        }
        this.inventory = startingInventory;
    }

    /**
     * Gets this builder's name.
     *
     * @return name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the current tile this builder is on.
     *
     * @return the current tile.
     */
    public Tile getCurrentTile() {
        return this.currentTile;
    }

    /**
     * Gets the builder's current inventory.
     *
     * @return blocks in the inventory, as a list.
     */
    public List<Block> getInventory() {
        return this.inventory;
    }

    /**
     * Places a block from the inventory on the top of the current tile.
     *
     * <p> Blocks can only be placed on tiles with less than 8 blocks,
     * or tiles with less than 3 blocks if the block is a ground block.
     *
     * <ul>
     * <li> If the inventoryIndex is &lt; 0 or ≥ the inventory size, throw
     *      an InvalidBlockException.</li>
     * <li> If there are 8 or more blocks on the current tile, throw a
     *      TooHighException.</li>
     * <li> If there are 3 or more blocks on the current tile and the indexed
     *      block is a ground block, throw a TooHighException.</li>
     * </ul>
     *
     * @param inventoryIndex the index in the inventory to place.
     * @throws InvalidBlockException if the inventoryIndex is out of the
     *                               inventory's range.
     * @throws TooHighException      if there are 8 blocks on the current tile
     *                               already, or the block is an instance of
     *                               GroundBlock and there are already 3
     *                               blocks on the current tile.
     */
    public void dropFromInventory(int inventoryIndex)
            throws InvalidBlockException, TooHighException {
        List<Block> inventory = this.getInventory();

        // Check index is within bounds of inventory size.
        // Alternatively, we could try/catch around .get().
        int inventorySize = inventory.size();
        if (inventoryIndex < 0 || inventoryIndex >= inventorySize) {
            throw new InvalidBlockException();
        }

        // Place the block and remove it from our inventory.
        // currentTile.placeBlock handles the height restrictions.
        Block block = inventory.get(inventoryIndex);
        this.currentTile.placeBlock(block);
        inventory.remove(inventoryIndex);
    }

    /**
     * Attempt to dig on the current tile and add the dug block to our
     * inventory.
     *
     * <p> If the top block is diggable, removes it from the tile. Then, if it
     * is carryable, adds it to the inventory.
     *
     * <p> Handles the following cases:
     * <ul>
     * <li> If there are no blocks on the current tile, throw a
     *      TooLowException. </li>
     * <li> If the top block is not diggable, throw an InvalidBlockException.
     *      </li>
     * <li> If the top block is not carryable, remove the block but do not add
     *      it to the inventory. </li>
     * </ul>
     *
     * @throws TooLowException       if there are no blocks on this tile.
     * @throws InvalidBlockException if the top block is not diggable
     */
    public void digOnCurrentTile()
        throws TooLowException, InvalidBlockException {
        // .dig() throws InvalidBlockException appropriately.
        Block dugBlock = this.currentTile.dig();
        if (dugBlock.isCarryable()) {
            this.inventory.add(dugBlock);
        }
    }

    /**
     * Checks if this builder can enter a tile from its current tile.
     *
     * <p>  Returns true if:
     * <ul>
     * <li> there is an exit from the current tile to the new tile, and</li>
     * <li> the height of the new tile is at most 1 block above or below this
     *      tile.</li>
     * </ul>
     *
     * If newTile is null return false.
     *
     * @param newTile the tile to test if we can enter.
     * @return true if the tile can be entered.
     */
    public boolean canEnter(Tile newTile) {
        // Special case for null tile.
        if (newTile == null) {
            return false;
        }

        // If there is no exit to newTile, it cannot be entered.
        if (!this.currentTile.getExits().values().contains(newTile)) {
            return false;
        }

        // There is an exit to newTile. In this case, it can be entered if
        // the height difference is <= 1.
        int newHeight = newTile.getBlocks().size();
        int currentHeight = this.currentTile.getBlocks().size();
        return Math.abs(newHeight - currentHeight) <= 1;
    }

    /**
     * Moves the builder to a new tile. The new tile must be reachable from
     * the current tile (i.e. canEnter(newTile) == true).
     *
     * <p> Throws NoExitException if it is impossible to move to newTile
     * from the current tile.
     *
     * @param newTile the tile to move to.
     * @throws NoExitException if moving to newTile is impossible.
     */
    public void moveTo(Tile newTile) throws NoExitException {
        if (!this.canEnter(newTile)) {
            throw new NoExitException();
        } else {
            this.currentTile = newTile;
        }
    }
}