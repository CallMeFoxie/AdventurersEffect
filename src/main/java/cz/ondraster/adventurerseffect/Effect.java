package cz.ondraster.adventurerseffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;

public class Effect {
   public static void scanInventory(EntityPlayer player) {
      ArrayList<Triggers> triggers = TriggerLoader.getTriggers();
      if (triggers == null)
         return;

      for (Triggers trigger : triggers) {
         scanInventory(player, trigger);
      }
   }

   public static void scanInventory(EntityPlayer player, Triggers trigger) {
      int itemSum = 0;
      for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
         ItemStack item = player.inventory.getStackInSlot(i);
         if (item == null)
            continue;

         if (item.getItem().getUnlocalizedName().equals(trigger.itemName) &&
               (!trigger.matchDamage || trigger.damageValue == item.getItemDamage()) &&
               (!trigger.matchNBT || ((trigger.nbtTagCompound == null && item.getTagCompound() == null) ||
                     trigger.nbtTagCompound.equals(item.getTagCompound())))) {
            itemSum += item.stackSize;
         }
      }

      if (trigger.amountOfItem == 0)
         trigger.amountOfItem = 1;

      if (itemSum >= trigger.amountOfItem)
         player.addPotionEffect(new PotionEffect(TriggerLoader.getPotion(trigger.potionName).id, trigger.durationSecs, trigger.amplifier));
   }

   public static void tryApplying(EntityPlayer player, ItemStack item) {
      if (TriggerLoader.getTriggers() == null)
         return;

      for (Triggers trigger : TriggerLoader.getTriggers()) {
         if (item.getItem().getUnlocalizedName().equals(trigger.itemName) &&
               (!trigger.matchDamage || trigger.damageValue == item.getItemDamage()) &&
               (!trigger.matchNBT || ((trigger.nbtTagCompound == null && item.getTagCompound() == null) ||
                     trigger.nbtTagCompound.equals(item.getTagCompound()))))
            scanInventory(player, trigger);
      }
   }
}
