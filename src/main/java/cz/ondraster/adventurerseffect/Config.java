package cz.ondraster.adventurerseffect;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
   private Configuration cfg;
   public static int ticksInterval = 40;

   public Config(String filename) {
      cfg = new Configuration(new File(filename));
      cfg.load();
      ticksInterval = cfg.getInt("ticksInterval", "config", ticksInterval, 1, 1000, "How often should the mod check for the item.");
      save();
   }

   public void save() {
      cfg.save();
   }
}
