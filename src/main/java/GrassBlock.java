/**
 * A grass block, subclass of GroundBlock and not carryable.
 */
public class GrassBlock extends GroundBlock {

    /**
     * Get the colour of a GrassBlock.
     * Always returns "green".
     *
     * @return "green"
     */
    public String getColour() {
        return "green";
    }

    /**
     * Get the type of a GrassBlock.
     * Always returns "grass".
     *
     * @return "grass"
     */
    public String getBlockType() {
        return "grass";
    }

    /**
     * GrassBlocks are not carryable.
     * Always returns false.
     *
     * @return false
     */
    public boolean isCarryable() {
        return false;
    }

}