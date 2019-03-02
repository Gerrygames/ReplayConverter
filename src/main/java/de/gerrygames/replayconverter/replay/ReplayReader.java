package de.gerrygames.replayconverter.replay;

import de.gerrygames.replayconverter.main.ReplayConverter;
import de.gerrygames.replayconverter.util.DummyChannel;
import de.gerrygames.replayconverter.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Pair;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.packets.Direction;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReplayReader {
	private DataInputStream dis;
	public ReplayInfo info;
	public int replayProtocol;
	private UserConnection user;
	private List<ByteBuf> sentPackets = new LinkedList<>();

	public ReplayReader(File file) throws IOException {

		try (ZipInputStream in = new ZipInputStream(new FileInputStream(file))) {
			ZipEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				if (entry.getName().equals("metaData.json")) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder builder = new StringBuilder();

					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line).append('\n');
					}

					String jsonText = builder.toString();
					this.info = ReplayConverter.GSON.fromJson(jsonText, ReplayInfo.class);
					this.info.setProtocol();

					this.replayProtocol = info.getProtocol();
				} else if (entry.getName().equals("recording.tmcpr")) {
					ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.buffer();
					int i;
					while ((i = in.read()) != -1) {
						buffer.writeByte(i);
					}
					this.dis = new DataInputStream(new ByteBufInputStream(buffer));
				}
			}
		}

		//ViaVersion
		this.user = new UserConnection(null) {
			@Override
			public void sendRawPacket(final ByteBuf buffer, boolean currentThread) {
				sentPackets.add(buffer);
			}

			@Override
			public Channel getChannel() {
				return DummyChannel.INSTANCE;
			}

			@Override
			public ChannelFuture sendRawPacketFuture(ByteBuf buffer) {
				sentPackets.add(buffer);
				return null;
			}

			@Override
			public void sendRawPacketToServer(ByteBuf packet, boolean currentThread) {
				;
			}
		};

		user.getStoredObjects().put(ClientWorld.class, new ClientWorld(user));

		new ProtocolPipeline(user);

		ProtocolInfo info = user.get(ProtocolInfo.class);
		info.setState(State.PLAY);
	}

	private boolean initPipeline(int protocol) {
		ProtocolPipeline pipeline = user.get(ProtocolInfo.class).getPipeline();
		List<Pair<Integer, Protocol>> path = ProtocolRegistry.getProtocolPath(protocol, this.replayProtocol);
		if (path == null) return false;
		path.forEach(pair -> pipeline.add(pair.getValue()));
		return true;
	}

	public byte[] convert(int protocol, ProgressMonitor progressMonitor) throws IOException {
		//ViaVersion
		if (!initPipeline(protocol)) return null;

		ByteBuf newreplay = UnpooledByteBufAllocator.DEFAULT.buffer();
		try {
			while (this.dis.available() > 0) {
				int time = this.dis.readInt();
				int length = this.dis.readInt();

				if (length == 0) continue;

				ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.buffer(length);
				try {
					while (length > 0) {  //Why is this stupid Netty ByteBuf not always reading all bytes?!
						length -= buffer.writeBytes(this.dis, length);
					}

					int packetId = PacketUtils.readVarInt(buffer);

					if (progressMonitor != null) {
						if (progressMonitor.isCanceled()) return null;
						double progress = ((double) time) / ((double) this.info.getDuration());
						progressMonitor.setProgress((int) (progress * 100));
						progressMonitor.setNote("Converting packet " + packetId + "...");
					}

					PacketWrapper packetWrapper = new PacketWrapper(packetId, buffer, this.user);

					ProtocolPipeline pipeline = user.get(ProtocolInfo.class).getPipeline();

					try {
						pipeline.transform(Direction.OUTGOING, State.PLAY, packetWrapper);
					} catch (CancelException ex) {
						continue;
					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}

					if (progressMonitor != null) {
						progressMonitor.setNote("Writing packet " + packetWrapper.getId() + "...");
					}

					ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.buffer();
					try {
						try {
							packetWrapper.writeToBuffer(buf);
						} catch (Exception ex) {
							ex.printStackTrace();
							continue;
						}
						newreplay.writeInt(time);
						newreplay.writeInt(buf.readableBytes());
						newreplay.writeBytes(buf);
					} finally {
						buf.release();
					}

					for (ByteBuf packet : sentPackets) {
						newreplay.writeInt(time);
						newreplay.writeInt(packet.readableBytes());
						newreplay.writeBytes(packet);
						packet.release();
					}

					sentPackets.clear();
				} finally {
					buffer.release();
				}
			}

			byte[] data = new byte[newreplay.readableBytes()];
			newreplay.readBytes(data);

			return data;
		} finally {
			newreplay.release();
		}
	}
}