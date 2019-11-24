package optic_fusion1.slimefunreloaded.multiblock;

import com.google.common.base.Objects;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;

/**
 * A base class to represent a machine composed of multiple blocks in the world.
 *
 * @author Parker "Choco" Hawke
 */
public class MultiBlock implements Keyed {

  private final NamespacedKey key;
  private final CuboidStructureBlueprint structure;
  private final BlockFace trigger;

  /**
   * Construct a new MultiBlock given a unique ID, structure blueprint and trigger face.
   *
   * @param key the unique key of the multiblock
   * @param structure the cuboid structure. See {@link StructureBlueprintFactory}
   * @param trigger the face on which the player must click to activate the structure
   */
  public MultiBlock(NamespacedKey key, CuboidStructureBlueprint structure, BlockFace trigger) {
    this.key = key;
    this.structure = structure;
    this.trigger = trigger;
  }

  @Override
  public NamespacedKey getKey() {
    return key;
  }

  /**
   * Get an immutable representation of this MultiBlock's structure.
   *
   * @return the structure
   */
  public CuboidStructureBlueprint getStructure() {
    return structure;
  }

  /**
   * Get the face on which the player must click to activate the structure
   *
   * @return the activation face
   */
  public BlockFace getTrigger() {
    return trigger;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key, structure, trigger);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof MultiBlock)) {
      return false;
    }

    MultiBlock other = (MultiBlock) obj;
    return Objects.equal(key, other.key) && Objects.equal(structure, other.structure) && trigger == other.trigger;
  }

}
