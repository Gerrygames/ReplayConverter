package de.gerrygames.replayconverter.viaversion;

public class CustomViaInjector implements us.myles.ViaVersion.api.platform.ViaInjector {
	@Override
	public void inject() {}

	@Override
	public void uninject() {}

	@Override
	public int getServerProtocolVersion() {
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
