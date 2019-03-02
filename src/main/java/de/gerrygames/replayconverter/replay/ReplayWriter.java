package de.gerrygames.replayconverter.replay;

import de.gerrygames.replayconverter.main.ReplayConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ReplayWriter {

	public static void writeReplayToFile(byte[] replaydata, File file, ReplayInfo replayInfo) {
		try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file))) {
			out.putNextEntry(new ZipEntry("recording.tmcpr"));
			out.write(replaydata);
			out.closeEntry();

			out.putNextEntry(new ZipEntry("metaData.json"));
			out.write(ReplayConverter.GSON.toJson(replayInfo).getBytes());
			out.closeEntry();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}