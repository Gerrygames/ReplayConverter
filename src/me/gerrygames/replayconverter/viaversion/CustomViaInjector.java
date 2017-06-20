package me.gerrygames.replayconverter.viaversion;

public class CustomViaInjector implements us.myles.ViaVersion.api.platform.ViaInjector {
	@Override
	public void inject() throws Exception {}

	@Override
	public void uninject() throws Exception {}

	@Override
	public int getServerProtocolVersion() throws Exception {
		return ViaVersionManager.serverProtocol;
	}

	@Override
	public String getEncoderName() {
		return null;
	}

	@Override
	public String getDecoderName() {
		return null;
	}
}
