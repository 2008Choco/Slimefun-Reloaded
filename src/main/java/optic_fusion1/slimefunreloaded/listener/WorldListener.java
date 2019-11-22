package optic_fusion1.slimefunreloaded.listener;

import java.util.logging.Level;
import optic_fusion1.slimefunreloaded.Slimefun;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {

  public WorldListener(SlimefunPlugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onWorldLoad(WorldLoadEvent e) {
    BlockStorage.getForcedStorage(e.getWorld());

    SlimefunPlugin.getWhitelist().setDefaultValue(e.getWorld().getName() + ".enabled", true);
    SlimefunPlugin.getWhitelist().setDefaultValue(e.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE", true);
    SlimefunPlugin.getWhitelist().save();
  }

  @EventHandler
  public void onWorldUnload(WorldUnloadEvent e) {
    BlockStorage storage = BlockStorage.getStorage(e.getWorld());
    if (storage != null) {
      storage.save(true);
    } else {
      Slimefun.getLogger().log(Level.SEVERE, "Could not save Slimefun Blocks for World \"" + e.getWorld().getName() + "\"");
    }
  }

}
