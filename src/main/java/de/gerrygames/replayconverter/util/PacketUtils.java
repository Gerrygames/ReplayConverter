package de.gerrygames.replayconverter.util;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketUtils {

	public static int readVarInt(ByteBuf buffer) {
		int number = 0;
		int round = 0;
		byte currentByte;

		do {
			currentByte = buffer.readByte();
			number |= (currentByte & 127) << round++ * 7;

			if (round > 5) {
				throw new RuntimeException("VarInt is too big");
			}
		} while ((currentByte & 128) == 128);

		return number;
	}

	public static void writeVarInt(ByteBuf buffer, int number) {
		while ((number & -128) != 0) {
			buffer.writeByte(number & 127 | 128);
			number >>>= 7;
		}

		buffer.writeByte(number);
	}
}