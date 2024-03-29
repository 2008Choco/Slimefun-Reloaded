package optic_fusion1.slimefunreloaded.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Utils {

  private Utils() {
  }

  public static FileConfiguration inputStreamToFileConfiguration(InputStream input) throws UnsupportedEncodingException {
    return YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(input, "UTF-8")));
  }

  public static void requireNonNull(Object... objects) {
    for (int i = 0; i < objects.length; i++) {
      if (objects[i] == null) {
        throw new NullPointerException("Array Number " + i + " is null");
      }
    }
  }

}
