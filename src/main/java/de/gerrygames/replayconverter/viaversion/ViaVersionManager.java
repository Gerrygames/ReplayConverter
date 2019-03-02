package de.gerrygames.replayconverter.viaversion;

import us.myles.ViaVersion.ViaManager;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;

public class ViaVersionManager {
	public static int serverProtocol = -1;

	public static void init() {
		Via.init(ViaManager.builder().platform(new CustomViaPlatform()).injector(new CustomViaInjector()).build());
		Via.getManager().getProviders().use(BulkChunkTranslatorProvider.class, new BulkChunkTranslatorProvider());
	}
}