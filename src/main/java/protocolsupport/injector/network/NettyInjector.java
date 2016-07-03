package protocolsupport.injector.network;

import me.hub.Main;
import net.minecraft.server.v1_10_R1.MinecraftServer;

public class NettyInjector {

	@SuppressWarnings("deprecation")
	public static void inject() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (MinecraftServer.getServer().ae()) {
			Main.logWarning("Native transport is enabled, this may cause issues. Disable it by setting use-native-transport in server.properties to false.");
		}
		BasicInjector.inject();
	}

}
