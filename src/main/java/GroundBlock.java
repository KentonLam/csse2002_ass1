/**
 * An abstract GroundBlock that enforces not moveable and diggable.
 */
public abstract class GroundBlock implements Block {

    /**
     * Whether the block can be moved.
     * GroundBlocks cannot be moved.
     *
     * @return false
     */
    public final boolean isMoveable() {
        return false;
    }

    /**
     * Whether the block is diggable.
     * GroundBlocks cannot be dug.
     *
     * @return true
     */
    public final boolean isDiggable() {
        return true;
    }

}