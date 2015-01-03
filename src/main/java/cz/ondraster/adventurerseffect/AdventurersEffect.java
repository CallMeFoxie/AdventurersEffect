package cz.ondraster.adventurerseffect;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import java.io.File;

@Mod(modid = "adventurerseffect", version = "1.1", name = "Adventurer's Effect", acceptableRemoteVersions = "*")
public class AdventurersEffect {

   public static String pathToConfigs;
   public boolean canBeLoaded;

   public static String configDir = "AdventurersEffect";

   private int ticksSinceLastUpdate = 0;
   Config myCfg;

   @Mod.EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      pathToConfigs = event.getModConfigurationDirectory().getAbsolutePath() + File.separator + configDir + File.separator;
      File cfgDir = new File(pathToConfigs);
      if (!cfgDir.exists())
         cfgDir.mkdir();

      TriggerLoader.filename = pathToConfigs + "Effects.cfg";
      canBeLoaded = (new File(TriggerLoader.filename).exists());

      myCfg = new Config(pathToConfigs + "Main.cfg");

   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent event) {
      MinecraftForge.EVENT_BUS.register(this);
      FMLCommonHandler.instance().bus().register(this);

      if (canBeLoaded)
         TriggerLoader.loadTriggers();
   }

   @Mod.EventHandler
   public void serverLoad(FMLServerStartingEvent event) {
      event.registerServerCommand(new CreateCommand());
   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.START || event.player.worldObj.isRemote)
         return;

      ticksSinceLastUpdate++;

      if (ticksSinceLastUpdate >= Config.ticksInterval) {
         Effect.scanInventory(event.player);
         ticksSinceLastUpdate = 0;
      }
   }

   @SubscribeEvent
   public void onPickupItem(EntityItemPickupEvent event) {
      Effect.tryApplying(event.entityPlayer, event.item.getEntityItem());
   }

   /*
   @SubscribeEvent
   public void onGuiEvent(GuiScreenEvent.DrawScreenEvent event) {
      if (!(event.gui instanceof GuiMainMenu))
         return;

      if (!canBeLoaded) {
         GuiMainMenu menu = (GuiMainMenu) event.gui;
         menu.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, I18n.format("error.no.effects"), (int) (menu.width / 2.2), 80, 0xFFFFFF);
      }
   }*/
}
