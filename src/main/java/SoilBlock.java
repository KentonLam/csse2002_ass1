/**
 * A soil block
 *
 */
public class SoilBlock extends GroundBlock {

    /**
     * 
     *
     */
    public SoilBlock() {
    }

    /**
     * Get the colour of a SoilBlock <br/>
     * Always returns "black"
     * 
     * @return "black"
     */
    public java.lang.String getColour() {
        return "black";
    }

    /**
     * Get the type of a SoilBlock <br/>
     * Always returns "soil"
     * 
     * @return "soil"
     */
    public java.lang.String getBlockType() {
        return "soil";
    }

    /**
     * SoilBlocks are carryable. <br/>
     * Always returns true
     * 
     * @return true
     */
    public boolean isCarryable() {
        return true;
    }

}