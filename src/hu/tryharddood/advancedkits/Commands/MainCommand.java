package advancedkits.Commands;

import advancedkits.AdvancedKits;
import advancedkits.InventoryApi.ItemBuilder;
import advancedkits.InventoryApi.PageInventory;
import advancedkits.Kits.Kit;
import advancedkits.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static advancedkits.Utils.I18n.tl;


/**
 * Class:
 *
 * @author TryHardDood
 */
public class MainCommand extends Subcommand
{
	@Override
	public String getPermission()
	{
		return Variables.KIT_USE_PERMISSION;
	}

	@Override
	public String getUsage()
	{
		return "/kit";
	}

	@Override
	public String getDescription()
	{
		return "Opens up the kit GUI";
	}

	@Override
	public int getArgs()
	{
		return 0;
	}

	@Override
	public boolean playerOnly()
	{
		return true;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player player = (Player) sender;

		PageInventory inv = new PageInventory(player);

		List<Kit>   kits  = AdvancedKits.getKitManager().getKits();
		ItemStack[] items = new ItemStack[kits.size()];

		Kit          kit;
		List<String> lore   = new ArrayList<>();
		int          delete = 0;
		for (int i = 0; i < kits.size(); i++)
		{
			if (!kits.get(i).isVisible() && (!player.hasPermission(Variables.KITADMIN_PERMISSION) && AdvancedKits.getKitManager().getUses(kits.get(i), player) >= kits.get(i).getUses()))
			{
				delete++;
				continue;
			}
			lore.clear();

			kit = kits.get(i);

			if (!AdvancedKits.getKitManager().CheckCooldown(player, kit))
			{
				lore.add("§8");
				lore.add(ChatColor.RED + "" + ChatColor.BOLD + tl("kituse_wait").replaceAll("\\{(\\D*?)\\}", "") + ":");
				lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "- " + AdvancedKits.getKitManager().getDelay(player, kit));
				lore.add("§8");
			}

			items[i - delete] = new ItemBuilder(kit.getIcon()).setTitle(ChatColor.translateAlternateColorCodes('&', kit.getName())).addLores(lore).addLores(AdvancedKits.getKitManager().getLores(player, kit)).build();
		}
		inv.setPages(items);
		inv.setTitle("Kits");
		inv.openInventory();
	}
}