package me.gerrygames.replayconverter.main;

import me.gerrygames.replayconverter.replay.ReplayReader;
import me.gerrygames.replayconverter.replay.ReplayInfo;
import me.gerrygames.replayconverter.replay.ReplayWriter;
import me.gerrygames.replayconverter.viaversion.ViaBackwardsManager;
import me.gerrygames.replayconverter.viaversion.ViaVersionManager;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class ReplayConverter {

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Replay File");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileFilter(new FileNameExtensionFilter("Replay Files", "mcpr"));
		int action = chooser.showOpenDialog(null);
		if (action == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			ViaVersionManager.init();
			ViaBackwardsManager.init();

			ReplayReader replayReader;
			try {
				replayReader = new ReplayReader(file);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			int replayProtocol = replayReader.replayProtocol;
			List<ProtocolVersion> protocolVersions = new ArrayList<>(ProtocolVersion.getProtocols());

			List<String> versions = new ArrayList<>();
			protocolVersions.sort(new Comparator<ProtocolVersion>() {
				@Override
				public int compare(ProtocolVersion v1, ProtocolVersion v2) {
					return v1.getId() > v2.getId() ? 1 : -1;
				}
			});

			for (ProtocolVersion protocolVersion : protocolVersions) {
				int id = protocolVersion.getId();
				if (id == 51 || id == 60 || id == 61 || id == 73 || id == 74 || id == 77 || id == 78) continue;
				if (id != replayProtocol && ProtocolRegistry.getProtocolPath(id, replayProtocol) != null) {
					versions.add(protocolVersion.getName() + " - " + protocolVersion.getId());
				}
			}

			if (versions.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Unsupported Version.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Collections.reverse(versions);

			String[] possibilities = new String[versions.size()];
			versions.toArray(possibilities);
			String s = (String) JOptionPane.showInputDialog(null, "Choose a version to convert to.", "Version", JOptionPane.PLAIN_MESSAGE, null, possibilities, null);

			if (s == null) return;

			s = s.substring(s.lastIndexOf(" ") + 1);
			int protocol = Integer.parseInt(s);

			ProgressMonitor progressMonitor = new ProgressMonitor(null, "This may take a few seconds:", "Converting...", 0, 100);
			progressMonitor.setMillisToDecideToPopup(0);
			progressMonitor.setProgress(0);

			byte[] data;
			try {
				data = replayReader.convert(protocol, progressMonitor);
			} catch (Exception e) {
				e.printStackTrace();
				progressMonitor.close();
				JOptionPane.showMessageDialog(null, "Error while reading/converting replay.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (data==null) {
				progressMonitor.close();
				JOptionPane.showMessageDialog(null, "Error while reading/converting replay.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			progressMonitor.setProgress(99);
			progressMonitor.setNote("Writing new replay to file.");

			ReplayInfo replayInfo = replayReader.info;
			replayInfo.setGenerator("ReplayConverter by Gerrygames");
			replayInfo.setProtocol(protocol);
			replayInfo.setFileFormatVersion(ReplayReader.PROTOCOL_FILEFORMAT.getOrDefault(protocol, -1));
			ProtocolVersion protocolVersion = ProtocolVersion.getProtocol(protocol);
			replayInfo.setMcversion(protocolVersion.getName());

			String oldname = file.getName();
			String filename = oldname.substring(0, oldname.toLowerCase().lastIndexOf(".")) + "-" + protocolVersion.getName() + ".mcpr";

			File outfile = new File(file.getParentFile(), filename);
			try {
				outfile.createNewFile();
				ReplayWriter.writeReplayToFile(data, outfile, replayInfo);
			} catch (Exception e) {
				progressMonitor.close();
				JOptionPane.showMessageDialog(null, "Error while writing replay to file.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			progressMonitor.close();

			JOptionPane.showMessageDialog(null, "The new replay file was saved.", "Done", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}