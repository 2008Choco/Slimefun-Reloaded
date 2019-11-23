package optic_fusion1.slimefunreloaded.listener;

import java.util.Optional;
import optic_fusion1.slimefunreloaded.SlimefunReloaded;
import optic_fusion1.slimefunreloaded.item.SlimefunReloadedItem;
import optic_fusion1.slimefunreloaded.item.impl.EnhancedFurnace;
import optic_fusion1.slimefunreloaded.recipe.MinecraftRecipe;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class FurnaceListener implements Listener {

  public FurnaceListener(SlimefunReloaded plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBurn(FurnaceBurnEvent e) {
    SlimefunReloadedItem furnace = BlockStorage.check(e.getBlock());

    if (furnace instanceof EnhancedFurnace && ((EnhancedFurnace) furnace).getFuelEfficiency() > 0) {
      e.setBurnTime(((EnhancedFurnace) furnace).getFuelEfficiency() * e.getBurnTime());
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onSmelt(FurnaceSmeltEvent e) {
    SlimefunReloadedItem furnace = BlockStorage.check(e.getBlock());

    if (furnace instanceof EnhancedFurnace) {
      Furnace f = (Furnace) e.getBlock().getState();
      int amount = f.getInventory().getSmelting().getType().toString().endsWith("_ORE") ? ((EnhancedFurnace) furnace).getOutput() : 1;
      Optional<ItemStack> result = Optional.ofNullable(f.getInventory().getResult());

      if (!result.isPresent()) {
        result = SlimefunReloaded.getMinecraftRecipes().getRecipeOutput(MinecraftRecipe.FURNACE, f.getInventory().getSmelting());
      }

      if (result.isPresent()) {
        ItemStack item = result.get();
        f.getInventory().setResult(new CustomItem(item, Math.min(item.getAmount() + amount, item.getMaxStackSize())));
      }
    }
  }

}
