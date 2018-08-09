/**
 * An interface for a block
 *
 */
public interface Block {

    /**
     * Get the colour of a block
     * 
     * @return the name of a colour
     */
    java.lang.String getColour();

    /**
     * Get the type of a block
     * 
     * @return the name of a block type (see subclasses)
     */
    java.lang.String getBlockType();

    /**
     * Is the block diggable? If so the block can be removed.
     * 
     * @return true if diggable, false if not
     */
    boolean isDiggable();

    /**
     * Is the block moveable? If so, the block can be shifted to adjacent tiles.
     * 
     * @return true if moveable, false if not
     */
    boolean isMoveable();

    /**
     * Is the block carryable? If so, the block can be added to the Builder's
     * inventory.
     * 
     * @return true if carryable, false if not
     */
    boolean isCarryable();

}