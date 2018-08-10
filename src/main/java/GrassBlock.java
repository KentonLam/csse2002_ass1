/**
 * A grass block
 *
 */
public class GrassBlock extends GroundBlock {

    /**
     * 
     *
     */
    public GrassBlock() {
    }

    /**
     * Get the colour of a GrassBlock <br/>
     * Always returns "green"
     * 
     * @return "green"
     */
    public java.lang.String getColour() {
        return "green";
    }

    /**
     * Get the type of a GrassBlock <br/>
     * Always returns "grass"
     * 
     * @return "grass"
     */
    public java.lang.String getBlockType() {
        return "grass";
    }

    /**
     * GrassBlocks are not carryable <br/>
     * Always returns false
     * 
     * @return false
     */
    public boolean isCarryable() {
        return false;
    }

}