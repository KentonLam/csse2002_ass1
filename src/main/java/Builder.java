import java.util.ArrayList;
import java.util.Map;

/**
 * A Player who modifies the map Manages an inventory of Blocks Maintains a
 * position in the map (by maintaining the current tile that the Builder is on)
 *
 */
public class Builder {

    private java.util.List<Block> inventory;
    private Tile currentTile;
    private String name;

    /**
     * Create a builder. <br/>
     * Set the name of the Builder (such that getName() == name) and the current
     * tile to startingTile (such that getCurrentTile() == startingTile).
     * 
     * @param name         name of the builder (returned by getName())- cannot be
     *                     null
     * @param startingTile the tile the builder starts in - cannot be null
     */
    public Builder(String name, Tile startingTile) {
        // Because the other constructor throws but this one doesn't, we 
        // can't chain the other one from here. 
        this.setInitialState(name, startingTile, new ArrayList<Block>());
    }

    /**
     * Create a builder <br/>
     * Set the name of the Builder (such that getName() == name) and the current
     * tile to startingTile (such that getCurrentTile() == startingTile). <br/>
     * Copy the starting inventory into the builder's inventory, such that the
     * contents of getInventory() are identical to startingInventory.
     * 
     * @param name              name of the builder (returned by getName()) - cannot
     *                          be null
     * @param startingTile      the tile the builder starts in - cannot be null
     * @param startingInventory the starting inventory (blocks) - cannot be null
     * @throws InvalidBlockException if for any Block (block) in startingInventory,
     *                               block.isCarryable() == false
     */
    public Builder(String name, Tile startingTile, 
            java.util.List<Block> startingInventory) throws InvalidBlockException {
        for (Block b : startingInventory) {
            if (!b.isCarryable())
                throw new InvalidBlockException();
        }
        this.setInitialState(name, startingTile, startingInventory);
    }

    /**
     * Helper method to store the initial state. Assumes all values are valid.
     * @param name              name of the builder (returned by getName()) - cannot
     *                          be null
     * @param startingTile      the tile the builder starts in - cannot be null
     * @param startingInventory the starting inventory (blocks) - cannot be null
     */
    private void setInitialState(String name, Tile startingTile, 
            java.util.List<Block> startingInventory) {
        this.name = name;
        this.currentTile = startingTile;
        this.inventory = startingInventory;
    }

    /**
     * Get the Builder's name
     * 
     * @return the Builder's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the current tile that the builder is on
     * 
     * @return the current tile
     */
    public Tile getCurrentTile() {
        return this.currentTile;
    }

    /**
     * What is in the Builder's inventory
     * 
     * @return blocks in the inventory
     */
    public java.util.List<Block> getInventory() {
        return this.inventory;
    }

    /**
     * Drop a block from inventory on the top of the current tile <br/>
     * Blocks can only be dropped on tiles with less than 8 blocks, or tiles with
     * less than 3 blocks if a GroundBlock. <br/>
     * Note: the current tile is that given by getCurrentTile() and the index should
     * refer to an item in the list returned by getInventory() <br/>
     * Handle the following cases:
     * <ol>
     * <li>If the inventoryIndex is &lt; 0 or ≥ the inventory size, throw an
     * InvalidBlockException.</li>
     * <li>If there are more than 8 blocks on the current tile, throw a
     * TooHighException.</li>
     * <li>If there are more than 3 blocks on the current tile, and the inventory
     * block is a GroundBlock, throw a TooHighException</li>
     * </ol>
     * Hint: call Tile.placeBlock, after checking the inventory
     * 
     * @param inventoryIndex the index in the inventory to place
     * @throws InvalidBlockException if the inventoryIndex is out of the inventory
     *                               range
     * @throws TooHighException      if there are 8 blocks on the current tile
     *                               already, or if the block is an instance of
     *                               GroundBlock and there are already 3 blocks on
     *                               the current tile.
     */
    public void dropFromInventory(int inventoryIndex) throws InvalidBlockException, TooHighException {
        java.util.List<Block> inventory = this.getInventory();
        int inventorySize = inventory.size();
        if (inventoryIndex < 0 || inventoryIndex >= inventorySize)
            throw new InvalidBlockException();

        Block block = inventory.get(inventoryIndex);
        this.currentTile.placeBlock(block);
        inventory.remove(inventoryIndex);
    }

    /**
     * Attempt to dig in the current tile and add tile to the inventory <br/>
     * If the top block (given by getCurrentTile().getTopBlock()) is diggable,
     * remove the top block of the tile and destroy it, or add it to the end of the
     * inventory (given by getInventory()). <br/>
     * Handle the following cases:
     * <ol>
     * <li>If there are no blocks on the current tile, throw a TooLowException</li>
     * <li>If the top block is not diggable, throw a InvalidBlockException</li>
     * <li>If the top block is not carryable, remove the block, but do not add it to
     * the inventory.</li>
     * </ol>
     * Hint: call Tile.dig()
     * 
     * @throws TooLowException       if there are no blocks on the current tile.
     * @throws InvalidBlockException if the top block is not diggable
     */
    public void digOnCurrentTile() throws TooLowException, InvalidBlockException {
        Block dugBlock;
        dugBlock = this.currentTile.dig();
        if (dugBlock.isCarryable())
            this.inventory.add(dugBlock);
    }

    /**
     * Check if the Builder can enter a tile from the current tile. <br/>
     * Returns true if:
     * <ol>
     * <li>the tiles are connected via an exit (i.e. there is an exit from the
     * current tile to the new tile), and</li>
     * <li>the height of the new tile (number of blocks) is the same or different by
     * 1 from the current tile (i.e. abs(current tile height - new tile) &lt;= 1)
     * </li>
     * </ol>
     * If newTile is null return false.
     * 
     * @param newTile the tile to test if we can enter
     * @return true if the tile can be entered
     */
    public boolean canEnter(Tile newTile) {
        if (newTile == null)
            return false;
        boolean canEnter = false;
        for (Tile tile : this.currentTile.getExits().values()) {
            if (tile.equals(newTile)) {
                int newHeight = tile.getBlocks().size();
                int currentHeight = this.currentTile.getBlocks().size();
                
                if (Math.abs(newHeight - currentHeight) <= 1) {
                    canEnter = true;
                    break;
                }

                // We could break here, but it is not guaranteed that there is
                // only one exit to a particular tile.
            }
        }
        return canEnter;
    }

    /**
     * move the builder to a new tile. <br/>
     * If canEnter(newTile) == true then change the builders current tile to be
     * newTile. (i.e. getCurrentTile() == newTile) <br/>
     * If canEnter(newTile) == false then throw a NoExitException.
     * 
     * @param newTile the tile to move to
     * @throws NoExitException if canEnter(newTile) == false
     */
    public void moveTo(Tile newTile) throws NoExitException {
        if (!this.canEnter(newTile))
            throw new NoExitException();
        else
            this.currentTile = newTile;
    }

}