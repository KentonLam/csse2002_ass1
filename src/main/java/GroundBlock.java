/**
 * An abstract GroundBlock that enforces not moveable and diggable.
 */
public abstract class GroundBlock implements Block {

    /**
     * Whether the block can be moved.
     * GroundBlocks can never be moved.
     *
     * @return false
     */
    public final boolean isMoveable() {
        return false;
    }

    /**
     * Whether the block is diggable.
     * GroundBlocks can never be dug.
     *
     * @return true
     */
    public final boolean isDiggable() {
        return true;
    }

}