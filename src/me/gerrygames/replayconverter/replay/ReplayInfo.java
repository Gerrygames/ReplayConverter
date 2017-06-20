package me.gerrygames.replayconverter.replay;

import lombok.Data;

import java.util.Set;

@Data
public class ReplayInfo {	
	private boolean singleplayer;
	private String serverName;
	private long duration;
	private long date;
	private String mcversion;
	private String fileFormat;
	private int fileFormatVersion;
	private String generator;
	private int selfId;
	private Set<String> players;
	private int protocol = -1;
}