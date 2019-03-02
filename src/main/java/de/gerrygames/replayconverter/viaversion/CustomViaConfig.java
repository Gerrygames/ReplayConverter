package de.gerrygames.replayconverter.viaversion;

import us.myles.ViaVersion.api.ViaVersionConfig;

import java.util.Collections;
import java.util.List;

public class CustomViaConfig implements ViaVersionConfig {
	@Override
	public boolean isCheckForUpdates() {
		return false;
	}

	@Override
	public boolean isPreventCollision() {
		return false;
	}

	@Override
	public boolean isNewEffectIndicator() {
		return false;
	}

	@Override
	public boolean isShowNewDeathMessages() {
		return false;
	}

	@Override
	public boolean isSuppressMetadataErrors() {
		return true;
	}

	@Override
	public boolean isShieldBlocking() {
		return false;
	}

	@Override
	public boolean isHologramPatch() {
		return false;
	}

	@Override
	public boolean isPistonAnimationPatch() {
		return false;
	}

	@Override
	public boolean isBossbarPatch() {
		return false;
	}

	@Override
	public boolean isBossbarAntiflicker() {
		return false;
	}

	@Override
	public boolean isUnknownEntitiesSuppressed() {
		return true;
	}

	@Override
	public double getHologramYOffset() {
		return 0;
	}

	@Override
	public boolean isAutoTeam() {
		return false;
	}

	@Override
	public boolean isBlockBreakPatch() {
		return false;
	}

	@Override
	public int getMaxPPS() {
		return -1;
	}

	@Override
	public String getMaxPPSKickMessage() {
		return null;
	}

	@Override
	public int getTrackingPeriod() {
		return -1;
	}

	@Override
	public int getWarningPPS() {
		return -1;
	}

	@Override
	public int getMaxWarnings() {
		return -1;
	}

	@Override
	public String getMaxWarningsKickMessage() {
		return null;
	}

	@Override
	public boolean isAntiXRay() {
		return false;
	}

	@Override
	public boolean isSendSupportedVersions() {
		return false;
	}

	@Override
	public boolean isStimulatePlayerTick() {
		return false;
	}

	@Override
	public boolean isItemCache() {
		return false;
	}

	@Override
	public boolean isNMSPlayerTicking() {
		return false;
	}

	@Override
	public boolean isReplacePistons() {
		return false;
	}

	@Override
	public int getPistonReplacementId() {
		return -1;
	}

	@Override
	public boolean isForceJsonTransform() {
		return false;
	}

	@Override
	public boolean is1_12NBTArrayFix() {
		return false;
	}

	@Override
	public boolean is1_13TeamColourFix() {
		return true;
	}

	@Override
	public boolean is1_12QuickMoveActionFix() {
		return false;
	}

	@Override
	public List<Integer> getBlockedProtocols() {
		return Collections.emptyList();
	}

	@Override
	public String getBlockedDisconnectMsg() {
		return null;
	}

	@Override
	public String getReloadDisconnectMsg() {
		return null;
	}

	@Override
	public boolean isSuppress1_13ConversionErrors() {
		return false;
	}

	@Override
	public boolean isDisable1_13AutoComplete() {
		return false;
	}

	@Override
	public boolean isMinimizeCooldown() {
		return true;
	}

	@Override
	public boolean isServersideBlockConnections() {
		return false;
	}

	@Override
	public String getBlockConnectionMethod() {
		return null;
	}

	@Override
	public boolean isReduceBlockStorageMemory() {
		return false;
	}

	@Override
	public boolean isStemWhenBlockAbove() {
		return false;
	}

	@Override
	public boolean isSnowCollisionFix() {
		return false;
	}

	@Override
	public int get1_13TabCompleteDelay() {
		return 0;
	}
}
