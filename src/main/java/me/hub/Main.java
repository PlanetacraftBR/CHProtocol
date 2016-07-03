package me.hub;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupport.commands.CommandHandler;
import protocolsupport.commands.ReloadCommandRemover;
import protocolsupport.injector.ServerInjector;
import protocolsupport.injector.network.NettyInjector;
import protocolsupport.logger.AsyncErrorLogger;
import protocolsupport.protocol.legacyremapper.LegacySound;
import protocolsupport.protocol.packet.ClientBoundPacket;
import protocolsupport.protocol.packet.ServerBoundPacket;
import protocolsupport.protocol.packet.handler.AbstractLoginListener;
import protocolsupport.protocol.pipeline.initial.InitialPacketDecoder;
import protocolsupport.protocol.typeremapper.id.IdRemapper;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.SpecificRemapper;
import protocolsupport.protocol.typeskipper.id.IdSkipper;
import protocolsupport.protocol.typeskipper.string.StringSkipper;
import protocolsupport.server.listeners.PlayerListener;
import protocolsupport.utils.netty.Allocator;
import protocolsupport.utils.netty.Compressor;

public class Main  extends JavaPlugin {

	
	
	@Override
	public void onLoad() {
		try {
			Allocator.init();
			Compressor.init();
			ServerBoundPacket.init();
			ClientBoundPacket.init();
			InitialPacketDecoder.init();
			AbstractLoginListener.init();
			LegacySound.init();
			IdSkipper.init();
			StringSkipper.init();
			SpecificRemapper.init();
			ServerInjector.inject();
			NettyInjector.inject();
			ReloadCommandRemover.remove();
			IdRemapper.init();
			AsyncErrorLogger.INSTANCE.start();
		} catch (Throwable t) {
			t.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public void onEnable() {
		getCommand("protocolsupport").setExecutor(new CommandHandler());
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		//me.security.version.Version.Registrar(this);
		
		
	}

	@Override
	public void onDisable() {
		Bukkit.shutdown();
		AsyncErrorLogger.INSTANCE.stop();
	}

	public static void logWarning(String message) {
		JavaPlugin.getPlugin(Main.class).getLogger().warning(message);
	}

	public static void logInfo(String message) {
		JavaPlugin.getPlugin(Main.class).getLogger().info(message);
	}

}
