/**
 * A wooden block or crate, is carryable, diggable and moveable.
 */
public class WoodBlock implements Block {

    /**
     * Get the colour of a WoodBlock.
     * Always returns "brown".
     *
     * @return "brown"
     */
    public String getColour() {
        return "brown";
    }

    /**
     * Get the type of a WoodBlock.
     * Always returns "wood".
     *
     * @return "wood"
     */
    public String getBlockType() {
        return "wood";
    }

    /**
     * A woodblock is diggable.
     * Always returns true.
     *
     * @return true
     */
    public boolean isDiggable() {
        return true;
    }

    /**
     * A woodblock is carryable.
     * Always returns true.
     *
     * @return true
     */
    public boolean isCarryable() {
        return true;
    }

    /**
     * A woodblock is moveable.
     * Always returns true.
     *
     * @return true
     */
    public boolean isMoveable() {
        return true;
    }

}