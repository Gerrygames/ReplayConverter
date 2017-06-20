package me.gerrygames.replayconverter.replay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ReplayWriter {

	public static void writeReplayToFile(byte[] replaydata, File file, ReplayInfo replayInfo) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);

		byte data[];
		String metaData;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		metaData = gson.toJson(replayInfo);

		ByteArrayOutputStream memOutput = new ByteArrayOutputStream();
		ZipOutputStream output = new ZipOutputStream(memOutput);
		{
			output.putNextEntry(new ZipEntry("recording.tmcpr"));
			output.write(replaydata);
			output.closeEntry();
		}
		{
			output.putNextEntry(new ZipEntry("metaData.json"));
			output.write(metaData.getBytes());
			output.closeEntry();
		}

		output.flush();
		output.close();

		fos.write(memOutput.toByteArray());
		fos.close();
	}
}