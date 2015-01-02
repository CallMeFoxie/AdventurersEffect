package cz.ondraster.adventurerseffect;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateCommand implements ICommand {
   @Override
   public String getCommandName() {
      return "aeadd";
   }

   @Override
   public String getCommandUsage(ICommandSender sender) {
      return "aeadd <usemeta> <usenbt> <potionName> <potionAmplifier> <potionDuration>";
   }

   @Override
   public List getCommandAliases() {
      return new ArrayList<String>();
   }

   @Override
   public void processCommand(ICommandSender sender, String[] args) throws CommandException {

      if (args.length != 5 || !(sender instanceof EntityPlayer))
         throw new CommandException("Incorrect number of args\n" + getCommandUsage(sender));

      EntityPlayer player = (EntityPlayer) sender;

      if (player.getCurrentEquippedItem() == null)
         throw new CommandException("You must be holding specified item.");

      if (!player.canCommandSenderUseCommand(2, "difficulty"))
         throw new CommandException("Not enough permissions!");

      boolean useMeta = false, useNBT = false;
      String potionName = args[2];
      int potionAmplifier = Integer.parseInt(args[3]);
      int potionDuration = Integer.parseInt(args[4]);

      if (args[0].equals("yes") || args[0].equals("true"))
         useMeta = true;
      if (args[1].equals("yes") || args[1].equals("true"))
         useNBT = true;

      if (TriggerLoader.getPotion(potionName) == null)
         throw new CommandException("Unknown potion effect!");

      try {
         TriggerLoader.addTrigger(potionName, potionAmplifier, potionDuration,
               player.getCurrentEquippedItem(), useMeta, useNBT);
      } catch (IOException e) {
         e.printStackTrace();
         throw new CommandException("Failed to save potion trigger :(");
      }


   }

   @Override
   public boolean canCommandSenderUseCommand(ICommandSender sender) {
      if (sender instanceof EntityPlayer) {
         return sender.canCommandSenderUseCommand(2, "difficulty");
      }
      return true;
   }

   @Override
   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      ArrayList<String> suggestions = new ArrayList<String>();
      switch (args.length) {
         case 1:
         case 2:
            suggestions.add("true");
            suggestions.add("false");
            break;
         case 3:
            suggestions = TriggerLoader.getPotionsNames();
            break;
      }

      return suggestions;
   }

   @Override
   public boolean isUsernameIndex(String[] args, int index) {
      return false;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
