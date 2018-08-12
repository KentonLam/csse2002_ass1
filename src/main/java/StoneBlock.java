/**
 * A stone block
 *
 */
public class StoneBlock implements Block {

    /**
     * Get the colour of a StoneBlock <br/>
     * Always returns "gray"
     *
     * @return "gray"
     */
    public String getColour() {
        return "gray";
    }

    /**
     * Get the type of a StoneBlock <br/>
     * Always returns "stone"
     *
     * @return "stone"
     */
    public String getBlockType() {
        return "stone";
    }

    /**
     * StoneBlocks are not diggable <br/>
     * Always returns false
     *
     * @return false
     */
    public boolean isDiggable() {
        return false;
    }

    /**
     * StoneBlocks are not moveable <br/>
     * Always returns false
     *
     * @return false
     */
    public boolean isMoveable() {
        return false;
    }

    /**
     * StoneBlocks are not carryable <br/>
     * Always returns false
     *
     * @return false
     */
    public boolean isCarryable() {
        return false;
    }

}