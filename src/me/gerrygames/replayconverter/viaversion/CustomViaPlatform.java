package me.gerrygames.replayconverter.viaversion;

import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.ViaVersionConfig;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.ViaVersion.api.platform.TaskId;
import us.myles.viaversion.libs.gson.JsonObject;

import java.util.UUID;
import java.util.logging.Logger;

public class CustomViaPlatform implements us.myles.ViaVersion.api.platform.ViaPlatform {
	private CustomViaConfig config = new CustomViaConfig();

	@Override
	public Logger getLogger() {
		return null;
	}

	@Override
	public String getPlatformName() {
		return "ReplayConverter";
	}

	@Override
	public String getPlatformVersion() {
		return null;
	}

	@Override
	public String getPluginVersion() {
		return "1.0";
	}

	@Override
	public TaskId runAsync(Runnable runnable) {
		return null;
	}

	@Override
	public TaskId runSync(Runnable runnable) {
		return null;
	}

	@Override
	public TaskId runRepeatingSync(Runnable runnable, Long aLong) {
		return null;
	}

	@Override
	public void cancelTask(TaskId taskId) {}

	@Override
	public ViaCommandSender[] getOnlinePlayers() {
		return new ViaCommandSender[0];
	}

	@Override
	public void sendMessage(UUID uuid, String s) {}

	@Override
	public boolean kickPlayer(UUID uuid, String s) {
		return false;
	}

	@Override
	public boolean isPluginEnabled() {
		return true;
	}

	@Override
	public ViaAPI getApi() {
		return null;
	}

	@Override
	public ViaVersionConfig getConf() {
		return config;
	}

	@Override
	public ConfigurationProvider getConfigurationProvider() {
		return null;
	}

	@Override
	public void onReload() {}

	@Override
	public JsonObject getDump() {
		return null;
	}

	@Override
	public boolean isOldClientsAllowed() {
		return false;
	}
}
