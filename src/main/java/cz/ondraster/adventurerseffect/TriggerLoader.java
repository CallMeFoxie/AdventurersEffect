package cz.ondraster.adventurerseffect;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TriggerLoader {

   private static ArrayList<Triggers> triggers;
   private static HashMap<String, Integer> potionMapping;

   public static String filename;


   public static void loadTriggers() {
      Gson gson = new Gson();
      triggers = new ArrayList<Triggers>();
      try {
         Reader rd = new FileReader(filename);
         Type listOfObjects = new TypeToken<ArrayList<Triggers>>() {
         }.getType();

         triggers = gson.fromJson(rd, listOfObjects);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void saveTriggers() throws IOException {


      Writer writer = new FileWriter(filename);
      Gson gson = new GsonBuilder().create();
      Type listOfObjects = new TypeToken<ArrayList<Triggers>>() {
      }.getType();
      String data = gson.toJson(triggers, listOfObjects);
      writer.write(data);
      writer.close();
   }

   public static ArrayList<Triggers> getTriggers() {
      return triggers;
   }

   public static Potion getPotion(String name) {
      if (potionMapping == null)
         loadPotions();

      return Potion.potionTypes[findPotion(name)];
   }

   public static void addTrigger(String potionEffect, int amplifier, int duration,
                                 ItemStack itemStack, boolean useMeta, boolean useNBT, int amountOfItem) throws IOException {
      Triggers trigger = new Triggers();
      trigger.amplifier = amplifier;
      trigger.damageValue = itemStack.getItemDamage();
      trigger.durationSecs = duration;
      trigger.itemName = itemStack.getItem().getUnlocalizedName();
      trigger.matchDamage = useMeta;
      trigger.matchNBT = useNBT;
      trigger.potionName = potionEffect;
      trigger.amountOfItem = amountOfItem;

      if (triggers == null)
         triggers = new ArrayList<Triggers>();

      triggers.add(trigger);

      saveTriggers();
   }

   private static int findPotion(String name) {
      return potionMapping.get(name);
   }

   private static void loadPotions() {
      if (potionMapping == null)
         potionMapping = new HashMap<String, Integer>();
      else
         potionMapping.clear();

      for (int i = 0; i < Potion.potionTypes.length; i++) {
         if (Potion.potionTypes[i] != null)
            potionMapping.put(Potion.potionTypes[i].getName(), i);
      }
   }

   public static ArrayList<String> getPotionsNames() {
      if (potionMapping == null)
         loadPotions();

      return new ArrayList<String>(potionMapping.keySet());
   }
}
