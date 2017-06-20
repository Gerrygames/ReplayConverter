package me.gerrygames.replayconverter.replay;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.netty.channel.ChannelFuture;
import me.gerrygames.replayconverter.util.PacketUtils;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Pair;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.Environment;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.packets.Direction;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import us.myles.viaversion.libs.gson.Gson;

import javax.swing.*;

public class ReplayReader {
	private DataInputStream dis;
	public ReplayInfo info;
	public int replayProtocol;
	private UserConnection user;
	private final Gson gson = new Gson();

	public static final Map<Integer, Integer> FILEFORMAT_PROTOCOL = new HashMap<>();

	static {
		FILEFORMAT_PROTOCOL.put(0, 47);
		FILEFORMAT_PROTOCOL.put(1, 47);
		FILEFORMAT_PROTOCOL.put(2, 110);
		FILEFORMAT_PROTOCOL.put(3, 210);
		FILEFORMAT_PROTOCOL.put(4, 315);
		FILEFORMAT_PROTOCOL.put(5, 316);
		FILEFORMAT_PROTOCOL.put(6, 335);
	}

	public static final Map<Integer, Integer> PROTOCOL_FILEFORMAT = new HashMap<>();

	static {
		PROTOCOL_FILEFORMAT.put(47, 1);
		PROTOCOL_FILEFORMAT.put(110, 2);
		PROTOCOL_FILEFORMAT.put(210, 3);
		PROTOCOL_FILEFORMAT.put(315, 4);
		PROTOCOL_FILEFORMAT.put(316, 5);
		PROTOCOL_FILEFORMAT.put(335, 6);

	}

	public ReplayReader(File file) throws Exception {
		//Read File
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		byte[] data = new byte[dis.available()];
		dis.readFully(data);
		dis.close();


		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(data));
		{
			ZipEntry entry;
			while ((entry = zipStream.getNextEntry()) != null) {
				if (entry.getName().equals("metaData.json")) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream));
					StringBuilder builder = new StringBuilder();

					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line).append('\n');
					}

					String jsonText = builder.toString();
					this.info = this.gson.fromJson(jsonText, ReplayInfo.class);

					int fileFormatVersion = info.getFileFormatVersion();
					this.replayProtocol = info.getProtocol() == -1 ? FILEFORMAT_PROTOCOL.getOrDefault(fileFormatVersion, -1) : info.getProtocol();
					break;
				}
			}
		}
		zipStream.close();

		zipStream = new ZipInputStream(new ByteArrayInputStream(data));
		{
			ZipEntry entry;
			while ((entry = zipStream.getNextEntry()) != null) {
				if (entry.getName().equals("recording.tmcpr")) {
					this.dis = new DataInputStream(zipStream);
					break;
				}
			}
		}

		//ViaVersion
		this.user = new UserConnection(null) {
			@Override
			public void sendRawPacket(final ByteBuf packet, boolean currentThread) {
			}

			@Override
			public ChannelFuture sendRawPacketFuture(ByteBuf packet) {
				return null;
			}
		};
		user.getStoredObjects().put(ClientWorld.class, new ClientWorld(user) {
			@Override
			public Environment getEnvironment() {
				return Environment.NORMAL;
			}
		});
		new ProtocolPipeline(user);
	}

	private boolean initPipeline(int protocol) {
		ProtocolPipeline pipeline = user.get(ProtocolInfo.class).getPipeline();
		List<Pair<Integer, Protocol>> path = ProtocolRegistry.getProtocolPath(protocol, this.replayProtocol);
		if (path == null) return false;
		for (int i = 0; i < path.size(); i++) {
			pipeline.add(path.get(i).getValue());
		}
		for (Protocol p : pipeline.pipes()) {
			p.init(user);
		}
		return true;
	}

	public byte[] convert(int protocol, ProgressMonitor progressMonitor) throws Exception {
		ByteBuf newreplay = Unpooled.buffer();

		//ViaVersion
		if (!initPipeline(protocol)) return null;

		try {
			while (this.dis.available() > 0) {
				int time = this.dis.readInt();
				int length = this.dis.readInt();
				byte[] data = new byte[length];
				this.dis.readFully(data, 0, length);

				if (length == 0) continue;

				if (progressMonitor != null) {
					if (progressMonitor.isCanceled()) return null;
					double progress = ((double) time) / ((double) this.info.getDuration());
					progressMonitor.setProgress((int) (progress * 100));
				}

				ByteBuf buffer = Unpooled.wrappedBuffer(data);

				int packetId = PacketUtils.readVarInt(buffer);

				ByteBuf oldPacket = buffer.copy(buffer.readerIndex(), buffer.readableBytes());
				buffer.release();

				PacketWrapper packetWrapper = new PacketWrapper(packetId, oldPacket, this.user);
				if (progressMonitor!=null) progressMonitor.setNote("Converting packet " + packetId + "...");

				us.myles.ViaVersion.packets.State state = us.myles.ViaVersion.packets.State.PLAY;

				ProtocolPipeline pipeline = user.get(ProtocolInfo.class).getPipeline();

				if (packetId == 38 && this.replayProtocol == 47) {
					ClientChunks clientChunks = user.get(ClientChunks.class);
					if (clientChunks == null) user.put(clientChunks = new ClientChunks(user));
					List packets = clientChunks.transformMapChunkBulk(packetWrapper);
					for (Object object : packets) {
						PacketWrapper packet = (PacketWrapper) object;

						ByteBuf oldChunk = Unpooled.buffer();
						packet.writeToBuffer(oldChunk);
						PacketUtils.readVarInt(oldChunk);

						PacketWrapper newpacket = new PacketWrapper(packet.getId(), oldChunk, user);

						pipeline.transform(Direction.OUTGOING, state, newpacket);

						buffer = Unpooled.buffer();
						newpacket.writeToBuffer(buffer);
						buffer.readerIndex(0);

						newreplay.writeInt(time);
						newreplay.writeInt(buffer.readableBytes());
						newreplay.writeBytes(buffer);

						buffer.release();
					}
					clientChunks.getBulkChunks().clear();
					clientChunks.getLoadedChunks().clear();
					continue;
				} else {
					try {
						pipeline.transform(Direction.OUTGOING, state, packetWrapper);
					} catch (CancelException e) {
						oldPacket.release();
						packetWrapper.clearInputBuffer();
						continue;
					}
				}

				buffer = Unpooled.buffer();
				packetWrapper.writeToBuffer(buffer);
				packetWrapper.clearInputBuffer();
				oldPacket.release();
				buffer.readerIndex(0);

				newreplay.writeInt(time);
				newreplay.writeInt(buffer.readableBytes());
				newreplay.writeBytes(buffer);

				buffer.release();
			}
		} catch (EOFException e) {}

		byte[] data = new byte[newreplay.readableBytes()];
		newreplay.readBytes(data);
		return data;
	}
}