/**
 * An interface describing functionality of a block.
 *
 */
public interface Block {

    /**
     * Gets the colour of a block.
     *
     * @return colour as a string.
     */
    String getColour();

    /**
     * Gets the type of a block.
     *
     * @return type of block as a string (see subclasses).
     */
    String getBlockType();

    /**
     * Whether the block is diggable.
     *
     * @return true if diggable, false if not.
     */
    boolean isDiggable();

    /**
     * Whether the block can be moved.
     *
     * @return true if moveable, false if not.
     */
    boolean isMoveable();

    /**
     * Whether the block can be carryed in the inventory.
     *
     * @return true if carryable, false if not.
     */
    boolean isCarryable();

}