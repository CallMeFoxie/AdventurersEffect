package cz.ondraster.adventurerseffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;

public class Effect {
   public static void scanInventory(EntityPlayer player, ArrayList<Triggers> effects) {
      if (effects == null)
         return;

      for (Triggers trigger : effects) {
         for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack item = player.inventory.getStackInSlot(i);
            if (item == null)
               continue;

            if (item.getUnlocalizedName().equals(trigger.itemName) &&
                  (!trigger.matchDamage || trigger.damageValue == item.getItemDamage()) &&
                  (!trigger.matchNBT || ((trigger.nbtTagCompound == null && item.getTagCompound() == null) ||
                        trigger.nbtTagCompound.equals(item.getTagCompound())))) {
               player.addPotionEffect(new PotionEffect(TriggerLoader.getPotion(trigger.potionName).id, trigger.durationSecs, trigger.amplifier));
            }
         }
      }
   }

   public static void tryApplying(EntityPlayer player, ItemStack itemStack) {
      for (Triggers trigger : TriggerLoader.getTriggers()) {
         if (itemStack.getUnlocalizedName().equals(trigger.itemName) &&
               (!trigger.matchDamage || trigger.damageValue == itemStack.getItemDamage()) &&
               (!trigger.matchNBT || ((trigger.nbtTagCompound == null && itemStack.getTagCompound() == null) ||
                     trigger.nbtTagCompound.equals(itemStack.getTagCompound()))))
            player.addPotionEffect(new PotionEffect(TriggerLoader.getPotion(trigger.potionName).id, trigger.durationSecs, trigger.amplifier));
      }
   }
}
