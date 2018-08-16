/**
 * A stone block, subclass of block. <i>Not</i> diggable, moveable or
 * carryable.
 *
 */
public class StoneBlock implements Block {

    /**
     * Get the colour of a StoneBlock.
     * Always returns "gray".
     *
     * @return "gray"
     */
    public String getColour() {
        return "gray";
    }

    /**
     * Get the type of a StoneBlock.
     * Always returns "stone".
     *
     * @return "stone"
     */
    public String getBlockType() {
        return "stone";
    }

    /**
     * StoneBlocks are not diggable.
     * Always returns false.
     *
     * @return false
     */
    public boolean isDiggable() {
        return false;
    }

    /**
     * StoneBlocks are not moveable.
     * Always returns false.
     *
     * @return false
     */
    public boolean isMoveable() {
        return false;
    }

    /**
     * StoneBlocks are not carryable.
     * Always returns false.
     *
     * @return false
     */
    public boolean isCarryable() {
        return false;
    }

}