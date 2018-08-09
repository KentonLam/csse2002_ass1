/**
 * An abstract GroundBlock that enforces not moveable and diggable
 *
 */
public abstract class GroundBlock extends java.lang.Object implements Block {

/**
 * 
 *
 */
public GroundBlock() {}

/**
 * Is the GroundBlock moveable?
 * GroundBlocks enforce not moving
 * @return false
 */
public final boolean isMoveable() {}

/**
 * Is the GroundBlock diggable?
 * GroundBlocks enforce allowing digging
 * @return true
 */
public final boolean isDiggable() {}

}