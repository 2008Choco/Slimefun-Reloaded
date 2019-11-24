package optic_fusion1.slimefunreloaded.multiblock;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

/**
 * A utility class to construct {@link CuboidStructureBlueprint} instances with Strings
 * and mappings from English characters to blocks and states.
 *
 * @author Parker "Choco" Hawke
 */
public final class StructureBlueprintFactory {

  /* Example:

      CuboidStructureBlueprint blueprint = StructureBlueprintFactory.create(new String[] { " N ", "WAW", " N " },
                                                                            new String[] { "EGE", "GGG", "EGE" },
                                                                            new String[] { "DDD", "DDD", "DDD" })
                                         .set('N', Material.OAK_LOG.createBlockData(b -> ((Orientable) b).setAxis(Axis.X)))
                                         .set('W', Material.OAK_LOG.createBlockData(b -> ((Orientable) b).setAxis(Axis.Z)))
                                         .set('A', Material.AIR).set('E', Material.EMERALD_BLOCK).set('G', Material.GOLD_BLOCK).set('D', Material.DIAMOND_BLOCK)
                                         .build();

      MultiBlock multiblock = new MultiBlock(new NamespacedKey(plugin, "MyMultiblock"), blueprint, BlockFace.NORTH);
  */

  private StructureBlueprintFactory() { /* No touchy touchy >:c */ }

  /**
   * Create a basic shape out of Strings in 3 dimensions. The order of the Strings and
   * characters matter and caution should be taken. Take for example the following structure:
   *
   * <pre>
   * .create(new String[] { " N ", "WAW", " N " },
   *         new String[] { "EGE", "GGG", "EGE" },
   *         new String[] { "DDD", "DDD", "DDD" })</pre>
   * The above will create a 3x3x3 structure where the first String[] is the top layer of the
   * structure, the second the middle layer, the last the bottom layer. If you would like 4 layers,
   * another String[] could be added so long as it's positioned according from top to bottom such that
   * the top is the highest y coordinate of the multiblock structure.
   * <p>
   * Secondly, every String in the y layer arrays correspond to a layer from front (the first string)
   * to the back (the last string) of the multiblock. In the above example, the first string in the second
   * array will have EGE on the middle y layer directly in front of the player if looked at from the front.
   * The third EGE will be on the back of the multiblock whereas the GGG will be in the middle of those two
   * slices.
   * <p>
   * Finally, every individual character in each String corresponds to a block type / state - defined by
   * the block data populator returned by this method - from left to right of the structure. Take for example
   * the first "D" in the bottom left of the matrix. This is the bottom left most block closest to the player
   * if looked at from the front. Whereas the last "D" in the bottom right of the matrix is the right most
   * block furthest from the player if looked at from the front (as it is in the 3rd z slice - or 3rd string
   * in the array).
   * <p>
   * This method is infinitely expandable and structures of any size can be created (within reason... the
   * server may not appreciate larger blueprints). If a 8x5x2 (x,y,z) structure is desired, 5 String arrays
   * containing 2 strings of length of 8.
   * <p>
   * <strong>IMPORTANT:</strong> All Strings and arrays MUST be the same size, otherwise an exception will be
   * thrown. It is crucial that no String or array is shorter or longer than any other. The amount of arrays,
   * however, is limitless so long as there is at least 1. null is an unacceptable value where an array or String
   * is expected.
   *
   * @param shape the shape of the blueprint
   *
   * @return a populator instance to assign characters to block states or materials
   */
  public static BlueprintBlockDataPopulator create(String[]... shape) {
    Preconditions.checkArgument(shape != null && shape.length > 0, "Multiblock shape cannot be null or empty");

    // Validate shape before allowing for a populator
    int height = shape.length, width = shape[0].length, length = shape[0][0].length(); // y, z, x

    Validate.isTrue(width > 0, "Width of multiblock must be greater than 0");
    Validate.isTrue(length > 0, "Length of multiblock must be greater than 0");

    // Validate consistency
    for (String[] sliceDepth : shape) {
      Validate.isTrue(sliceDepth != null, "Received null depth slice");
      Validate.isTrue(sliceDepth.length == width, "Depth of blueprint does not match for all slices");

      for (String sliceLength : sliceDepth) {
        Validate.isTrue(sliceLength != null, "Received null length slice");
        Validate.isTrue(sliceLength.length() == length, "Length of blueprint does not match for all slices");
      }
    }

    return new BlueprintBlockDataPopulator(shape, length, height, width);
  }


  /**
   * A populator to assign {@link BlockData} or {@link Material} values to characters in a
   * blueprint.
   *
   * @see StructureBlueprintFactory#create(String[][])
   */
  public static final class BlueprintBlockDataPopulator {

    private final Map<Character, BlockData> data = new HashMap<>();
    private final String[][] raw;
    private final int length, height, width; // x, y, z

    private BlueprintBlockDataPopulator(String[][] raw, int length, int height, int width) {
      this.raw = raw;
      this.length = length;
      this.height = height;
      this.width = width;
    }

    /**
     * Assign a character to the specified {@link BlockData}.
     *
     * @param character the character to assign
     * @param value the block data
     *
     * @return this instance. Allows for chained method calls
     */
    public BlueprintBlockDataPopulator set(char character, BlockData value) {
      this.data.put(character, value);
      return this;
    }

    /**
     * Assign a character to the specified {@link Material}.
     *
     * @param character the character to assign
     * @param value the material
     *
     * @return this instance. Allows for chained method calls
     */
    public BlueprintBlockDataPopulator set(char character, Material value) {
      return set(character, value.createBlockData());
    }

    /**
     * Validate and build the {@link CuboidStructureBlueprint} instance based on the blocks
     * assigned and the raw data supplied by the factory.
     *
     * @return the constructed blueprint instance
     */
    public CuboidStructureBlueprint build() {
      // Construct block data
      BlockData[][][] blocks = new BlockData[length][width][height];

      for (int y = 0; y < height; y++) {
        for (int z = 0; z < width; z++) {
          for (int x = 0; x < length; x++) {
            char element = raw[y][z].charAt(x);
            if (Character.isWhitespace(element)) {
              blocks[x][y][z] = null;
              continue;
            }

            BlockData blockResult = data.get(element);
            if (blockResult == null) {
              throw new IllegalStateException("Undefined blueprint character: " + element);
            }

            blocks[x][y][z] = blockResult;
          }
        }
      }

      return new CuboidStructureBlueprint(length, height, width, blocks);
    }

  }

}
