package me.hub;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import me.hub.API.Util.BarAPI;
import me.hub.API.Util.UtilHolo;
import me.hub.API.Util.UtilServer;
import me.hub.atualizar.Atualizar;
import me.hub.atualizar.ModosUpdate;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.events.PlayerLoginStartEvent;



public class Version extends MiniPlugin {

	
	
	public Version(JavaPlugin plugin) {
		super("Version", plugin);
		// TODO Auto-generated constructor stub
	}

	static String modo = "liberar";
	static String kick = "&cDesculpe mais você não esta utilizando a versão correta";
	static List<String> versions = new ArrayList<String>();
	static boolean ativo = false;
	
	@EventHandler
	public void Update(Atualizar event)
	{
		if (ativo)
			return;
		if (event.getType() == ModosUpdate.FASTER)
		{
			Registrar(Main.plugin);
			System.out.print("Ativando sistema do CHProtocol!");
		  ativo = true;
		}
		
	}
	

	
	@EventHandler
	public void Remover(Atualizar event)
	{
		if (event.getType() == ModosUpdate.FASTER)
		{
			for (Player jog : UtilServer.Jogadores())
			{
				jog.getInventory().remove(Material.SHIELD);
			}
		}
	}
	
	@EventHandler
	public void Teste(PlayerInteractEvent event)
	{
		if (event.getMaterial() == Material.SHIELD)
		{
			event.setCancelled(true);
			event.getPlayer().getInventory().remove(Material.SHIELD);
		}
	}
	
	
	@EventHandler
	public void Teste(PlayerJoinEvent event)
	{
		if (ProtocolSupportAPI.getProtocolVersion(event.getPlayer()) != ProtocolVersion.MINECRAFT_1_9)
			BarAPI.outros.add(event.getPlayer());
		if (ProtocolSupportAPI.getProtocolVersion(event.getPlayer()) == ProtocolVersion.MINECRAFT_1_7_10)
			{
			UtilHolo.AddPlayer1_7(event.getPlayer());
			UtilHolo.ArmoStand();
			
			}
	}
	
	public static void Registrar(JavaPlugin plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(new Version(plugin), plugin);
		versions.add(ProtocolVersion.MINECRAFT_1_8.toString());
		versions.add(ProtocolVersion.MINECRAFT_1_9.toString());
		plugin.getConfig().addDefault("modo", modo);
		plugin.getConfig().addDefault("kick_msg", kick);
		plugin.getConfig().addDefault("versions", versions);
		
	    plugin.getConfig().options().copyDefaults(true);
	    plugin.saveConfig();
	    
	    modo = plugin.getConfig().getString("modo");
	    kick = plugin.getConfig().getString("kick_msg");
	    versions = plugin.getConfig().getStringList("versions");
	    
	    ShapelessRecipe golden = new ShapelessRecipe(new ItemStack(Material.GOLDEN_APPLE,1,(byte)1));
	    golden.addIngredient(Material.APPLE);
	    golden.addIngredient(Material.GOLD_BLOCK, 8);
	    plugin.getServer().addRecipe(golden);
	    System.out.print(versions);
	    
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerLoginStartEvent e){
		System.out.print(e.getName() + " Protocol Minecraft: " + ProtocolSupportAPI.getProtocolVersion(e.getAddress()));
		if(modo.equalsIgnoreCase("liberar")){
			if(!versions.contains(""+ ProtocolSupportAPI.getProtocolVersion(e.getAddress()))){
				 e.denyLogin("" + kick.replace("&", "§"));
				 
				return;
				
			}
			
		}
		if(modo.equalsIgnoreCase("block")){{
			if(versions.contains("" + ProtocolSupportAPI.getProtocolVersion(e.getAddress()))){
				 e.denyLogin("" + kick.replace("&", "§"));
				return;
			}
		}
	}
	}
	
	

	
}
