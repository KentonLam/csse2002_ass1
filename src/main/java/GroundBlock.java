/**
 * GroundBlock
 */
public abstract class GroundBlock implements Block {
    public GroundBlock() {

    }

    @Override
    public final boolean isDiggable() {
        return true;
    }

    @Override
    public final boolean isMoveable() {
        return false;
    }
}