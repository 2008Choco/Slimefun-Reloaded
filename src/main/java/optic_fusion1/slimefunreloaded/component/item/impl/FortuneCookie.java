package optic_fusion1.slimefunreloaded.component.item.impl;

import static com.massivecraft.factions.P.p;
import java.util.List;
import java.util.Random;
import optic_fusion1.slimefunreloaded.Slimefun;
import optic_fusion1.slimefunreloaded.category.type.Category;
import optic_fusion1.slimefunreloaded.component.item.SlimefunReloadedConsumable;
import optic_fusion1.slimefunreloaded.util.I18n;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FortuneCookie extends SlimefunReloadedConsumable {

  private static final Random random = new Random();
  
  public FortuneCookie(NamespacedKey key, Category category, ItemStack item) {
    super(key, category, item);
  }

  @Override
  public boolean onConsume(Player player, ItemStack item) {
    List<String> messages = I18n.getMessages("messages.fortune-cookie");
    String message = messages.get(random.nextInt(messages.size()));
    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    return true;
  }

}
