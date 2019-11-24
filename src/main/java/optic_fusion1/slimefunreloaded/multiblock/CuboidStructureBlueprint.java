package optic_fusion1.slimefunreloaded.multiblock;

import com.google.common.base.Preconditions;

import org.bukkit.block.data.BlockData;

/**
 * A collection of blocks defined in 3 dimensional space.
 *
 * @author Parker "Choco" Hawke
 */
public final class CuboidStructureBlueprint {

  private final int sizeX, sizeY, sizeZ;
  private final BlockData[][][] blocks;

  // Package private. Only accessible to StructureBlueprintFactory
  CuboidStructureBlueprint(int sizeX, int sizeY, int sizeZ, BlockData[][][] blocks) {
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.sizeZ = sizeZ;
    this.blocks = blocks;
  }

  /**
   * Get the {@link BlockData} at the given position. This is a 0-indexed position, therefore
   * they must lay within 0 (inclusive) and the maximum size of the axis (exclusive). This
   * is a nullable value.
   *
   * @param x the x index
   * @param y the y index
   * @param z the z index
   *
   * @return the block data at the requested position. null if any (wildcarded) block
   */
  public BlockData getBlockAt(int x, int y, int z) {
    this.checkBounds(x, y, z);

    BlockData data = blocks[x][y][z];
    return (data != null) ? data.clone() : null;
  }

  /**
   * Check whether the given position is a wildcarded block implying any block may be placed
   * at that position though still consider this a valid blueprint. This is a 0-indexed position,
   * therefore they must lay within 0 (inclusive) and the maximum size of the axis (exclusive).
   *
   * @param x the x index
   * @param y the y index
   * @param z the z index
   *
   * @return true if wildcarded, false otherwise
   */
  public boolean isWildcard(int x, int y, int z) {
    this.checkBounds(x, y, z);
    return blocks[x][y][z] == null;
  }

  private void checkBounds(int x, int y, int z) {
    Preconditions.checkArgument(x >= 0 && x < sizeX, "x out of bounds. Expected 0 - %d (exclusive), got: (%d)", sizeX, x);
    Preconditions.checkArgument(y >= 0 && y < sizeY, "y out of bounds. Expected 0 - %d (exclusive), got: (%d)", sizeX, x);
    Preconditions.checkArgument(z >= 0 && z < sizeZ, "z out of bounds. Expected 0 - %d (exclusive), got: (%d)", sizeX, x);
  }

}
