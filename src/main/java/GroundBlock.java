/**
 * An abstract GroundBlock that is diggable but not moveable.
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
     * GroundBlocks can always be dug.
     *
     * @return true
     */
    public final boolean isDiggable() {
        return true;
    }

}