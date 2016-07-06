package protocolsupport.injector.network;


import me.hub.CHProtocol;
import net.minecraft.server.v1_10_R1.MinecraftServer;

public class NettyInjector {

	@SuppressWarnings("deprecation")
	public static void inject() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (MinecraftServer.getServer().ae()) {
			CHProtocol.logWarning("Native transport is enabled, this may cause issues. Disable it by setting use-native-transport in server.properties to false.");
		}
		BasicInjector.inject();
	}

}
