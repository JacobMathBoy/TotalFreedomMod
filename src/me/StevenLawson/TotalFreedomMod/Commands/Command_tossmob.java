package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_UserInfo;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.ONLY_IN_GAME, ignore_permissions = false)
public class Command_tossmob extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!TotalFreedomMod.tossmobEnabled)
        {
            sender.sendMessage(ChatColor.RED + "Tossmob is currently disabled.");
            return true;
        }

        TFM_UserInfo playerData = TFM_UserInfo.getPlayerData(sender_p);

        EntityType creature = EntityType.PIG;
        if (args.length >= 1)
        {
            if (TFM_Util.isStopCommand(args[0]))
            {
                playerData.disableMobThrower();
                sender.sendMessage(ChatColor.GREEN + "MobThrower is disabled.");
                return true;
            }

            if (args[0].equalsIgnoreCase("list"))
            {
                sender.sendMessage(ChatColor.GREEN + "Supported mobs: " + StringUtils.join(TFM_Util.mobtypes.keySet(), ", "));
                return true;
            }

            try
            {
                creature = TFM_Util.getEntityType(args[0]);
            }
            catch (Exception ex)
            {
                sender.sendMessage(ChatColor.RED + args[0] + " is not a supported mob type. Using a pig instead.");
                sender.sendMessage(ChatColor.RED + "By the way, you can type /tossmob list to see all possible mobs.");
                creature = EntityType.PIG;
            }
        }

        double speed = 1.0;
        if (args.length >= 2)
        {
            try
            {
                speed = Double.parseDouble(args[1]);
            }
            catch (NumberFormatException nfex)
            {
            }
        }

        if (speed < 1.0)
        {
            speed = 1.0;
        }
        else if (speed > 5.0)
        {
            speed = 5.0;
        }

        playerData.enableMobThrower(creature, speed);
        sender.sendMessage(ChatColor.GREEN + "MobThrower is enabled. Creature: " + creature + " - Speed: " + speed + ".");
        sender.sendMessage(ChatColor.GREEN + "Left click while holding a stick to throw mobs!");
        sender.sendMessage(ChatColor.GREEN + "Type '/tossmob off' to disable.  -By Madgeek1450");

        sender_p.setItemInHand(new ItemStack(Material.STICK, 1));

        return true;
    }
}
