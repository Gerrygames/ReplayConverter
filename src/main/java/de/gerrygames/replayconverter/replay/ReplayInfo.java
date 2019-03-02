package de.gerrygames.replayconverter.replay;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class ReplayInfo {
	public static final Map<Integer, Integer> PROTOCOL_BY_FILE_FORMAT_VERSION = new HashMap<>();
	static {
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(0, 47);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(1, 47);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(2, 110);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(3, 210);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(4, 315);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(5, 316);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(6, 335);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(7, 338);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(9, 340);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(10, 393);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(11, 401);
		PROTOCOL_BY_FILE_FORMAT_VERSION.put(12, 404);
	}

	public static final Map<String, Integer> PROTOCOL_BY_MC_VERSION = new HashMap<>();
	static {
		PROTOCOL_BY_MC_VERSION.put("1.8", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.1", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.2", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.3", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.4", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.5", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.6", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.7", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.8", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.9", 47);
		PROTOCOL_BY_MC_VERSION.put("1.8.x", 47);
		PROTOCOL_BY_MC_VERSION.put("1.9", 107);
		PROTOCOL_BY_MC_VERSION.put("1.9.1", 108);
		PROTOCOL_BY_MC_VERSION.put("1.9.2", 109);
		PROTOCOL_BY_MC_VERSION.put("1.9.3", 110);
		PROTOCOL_BY_MC_VERSION.put("1.9.4", 110);
		PROTOCOL_BY_MC_VERSION.put("1.10", 210);
		PROTOCOL_BY_MC_VERSION.put("1.10.1", 210);
		PROTOCOL_BY_MC_VERSION.put("1.10.2", 210);
		PROTOCOL_BY_MC_VERSION.put("1.10.x", 210);
		PROTOCOL_BY_MC_VERSION.put("1.11", 315);
		PROTOCOL_BY_MC_VERSION.put("1.11.1", 316);
		PROTOCOL_BY_MC_VERSION.put("1.11.2", 316);
		PROTOCOL_BY_MC_VERSION.put("1.12", 335);
		PROTOCOL_BY_MC_VERSION.put("1.12.1", 338);
		PROTOCOL_BY_MC_VERSION.put("1.12.2", 340);
		PROTOCOL_BY_MC_VERSION.put("1.13", 393);
		PROTOCOL_BY_MC_VERSION.put("1.13.1", 401);
		PROTOCOL_BY_MC_VERSION.put("1.13.2", 404);
	}

	public static int getFileFormatVersion(int protocol) {
		if (protocol > 404) {
			return 13;
		} else if (protocol == 404) {
			return 12;
		} else if (protocol == 401) {
			return 11;
		} else if (protocol == 393) {
			return 10;
		} else if (protocol == 340) {
			return 9;
		} else if (protocol == 338) {
			return 7;
		} else if (protocol == 335) {
			return 6;
		} else if (protocol == 316) {
			return 5;
		} else if (protocol == 315) {
			return 4;
		} else if (protocol == 210) {
			return 3;
		} else if (protocol == 110) {
			return 2;
		} else if (protocol == 47) {
			return 1;
		}
		return 13;
	}

	private boolean singleplayer;
	private String serverName;
	private long duration;
	private long date;
	private String mcversion;
	private String fileFormat;
	private int fileFormatVersion = -1;
	private String generator;
	private int selfId;
	private Set<String> players;
	private int protocol = -1;

	public void setProtocol() {
		if (protocol >= 0) return;
		if (ReplayInfo.PROTOCOL_BY_FILE_FORMAT_VERSION.containsKey(fileFormatVersion)) {
			this.protocol = ReplayInfo.PROTOCOL_BY_FILE_FORMAT_VERSION.get(this.fileFormatVersion);
		} else if (mcversion != null && PROTOCOL_BY_MC_VERSION.containsKey(mcversion = mcversion.toLowerCase())) {
			this.protocol = PROTOCOL_BY_MC_VERSION.get(mcversion);
		} else {
			this.protocol = -1;
		}
	}
}