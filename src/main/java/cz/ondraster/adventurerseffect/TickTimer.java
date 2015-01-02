package cz.ondraster.adventurerseffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class TickTimer implements IExtendedEntityProperties {
   public int tickCounter;

   private static final String PROP_NAME = "userTickTimerProp";
   private EntityPlayer player;

   public TickTimer(EntityPlayer player) {
      this.player = player;
   }

   @Override
   public void saveNBTData(NBTTagCompound compound) {

   }

   @Override
   public void loadNBTData(NBTTagCompound compound) {

   }

   @Override
   public void init(Entity entity, World world) {
      tickCounter = 0;
   }

   public static void register(EntityPlayer player) {
      player.registerExtendedProperties(PROP_NAME, new TickTimer(player));
   }
}
