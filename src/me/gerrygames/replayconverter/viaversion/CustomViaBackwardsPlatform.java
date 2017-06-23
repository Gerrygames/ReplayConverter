package me.gerrygames.replayconverter.viaversion;

import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;

import java.util.logging.Logger;

public class CustomViaBackwardsPlatform implements ViaBackwardsPlatform {

	public CustomViaBackwardsPlatform() {
		ViaBackwards.init(this);
		init();
	}

	@Override
	public Logger getLogger() {return null;}

	@Override
	public boolean isOutdated() {return false;}

	@Override
	public void disable() {}

}