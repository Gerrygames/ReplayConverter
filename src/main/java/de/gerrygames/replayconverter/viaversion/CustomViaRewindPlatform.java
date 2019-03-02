package de.gerrygames.replayconverter.viaversion;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import us.myles.ViaVersion.api.Via;

import java.util.logging.Logger;

public class CustomViaRewindPlatform implements ViaRewindPlatform {

	public CustomViaRewindPlatform() {
		init(new ViaRewindConfig() {
			@Override
			public CooldownIndicator getCooldownIndicator() {
				return CooldownIndicator.DISABLED;
			}

			@Override
			public boolean isReplaceAdventureMode() {
				return true;
			}

			@Override
			public boolean isReplaceParticles() {
				return true;
			}
		});
	}

	@Override
	public Logger getLogger() {
		return Via.getPlatform().getLogger();
	}
}
