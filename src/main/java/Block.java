/**
 * An interface for a block.
 */
public interface Block {
    /**
     * Gets the type of a block.
     * @return the name of a block type (see subclasses).
     */
    String getBlockType();
    /**
     * Gets the colour of the block.
     * @return string representing a colour.
     */
    String getColour();
    /**
     * Whether the block can be carried.
     * @return true if carryable, false if not.
     */
    boolean isCarryable();
    /**
     * Whether the block can be dug.
     * @return true if diggable, false if not.
     */
    boolean isDiggable();
    /**
     * Whether the block can be moved.
     * @return true if movaeble, false if not.
     */
    boolean isMoveable();
}