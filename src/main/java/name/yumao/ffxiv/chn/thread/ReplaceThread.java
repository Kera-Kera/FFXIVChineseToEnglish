package name.yumao.ffxiv.chn.thread;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;
import name.yumao.ffxiv.chn.replace.ReplaceEXDF;
import name.yumao.ffxiv.chn.replace.ReplaceFont;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.swing.TextPatchPanel;
import name.yumao.ffxiv.chn.util.res.Config;

public class ReplaceThread implements Runnable {
	private String resourceFolder;
	private TextPatchPanel textPatchPanel;
	// private List<TeemoUpdateVo> updates;
	private String slang;
	private String flang;
	private String rfont;
	private String rtext;
	
	public ReplaceThread(String resourceFolder, TextPatchPanel textPatchPanel) {
		this.resourceFolder = resourceFolder;
		this.textPatchPanel = textPatchPanel;
		// this.updates = updates;
		this.slang = Config.getProperty("SLanguage");
		this.flang = Config.getProperty("FLanguage");
		this.rfont = Config.getProperty("ReplaFont");
		this.rtext = Config.getProperty("ReplaText");
	}
	
	public static boolean hasCsvFiles(String directoryPath) {
		File directory = new File(directoryPath);
		if (!directory.exists() || !directory.isDirectory()) {
			return false;
		}
		
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file: files) {
				if (file.isDirectory()) {
					if (hasCsvFiles(file.getAbsolutePath())) {
						return true;
					}
				} else if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void run() {
		Logger log = Logger.getLogger("GPLogger");
		
		try {
			this.textPatchPanel.replaceButton.setEnabled(false);
			PercentPanel percentPanel = new PercentPanel("Patching...");
			if (this.rfont.equals("1")) {
				new ReplaceFont(this.resourceFolder + File.separator + "000000.win32.index", "resource" + File.separator + "font", percentPanel).replace();
			} else {
				log.info("Skip replacing font files.");
			}
			if (this.rtext.equals("1")) {
				if ((this.flang.equals("CSV")) && hasCsvFiles("resource" + File.separator + "rawexd")) {
					log.info("Start patching with CSV files.");
					(new ReplaceEXDF(this.resourceFolder + File.separator + "0a0000.win32.index", "resource" + File.separator + "rawexd" + File.separator + "Achievement.csv", percentPanel)).replace();
				} else if (!(this.flang.equals("CSV")) && (new File("resource" + File.separator + "text" + File.separator + "0a0000.win32.index")).exists()) {
					log.info("Start patching with 0a0000 files.");
					(new ReplaceEXDF(this.resourceFolder + File.separator + "0a0000.win32.index", "resource" + File.separator + "text" + File.separator + "0a0000.win32.index", percentPanel)).replace();
				} else {
					System.out.println("No resource files detected!");
					log.severe("No resource files detected!");
				}
			} else {
				log.info("Skip replacing text.");
			}
			JOptionPane.showMessageDialog(null, "<html><body>Patch complete!</body></html>", "Done", -1);
			log.info("Patch finished.");
			percentPanel.dispose();
			this.textPatchPanel.replaceButton.setEnabled(true);
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(null, "<html><body>An error occurred!</body></html>", "Patch Error", 0);
			log.severe("Patch failed!");
			log.log(Level.SEVERE, "Error Messages:", exception);
			exception.printStackTrace();
		} 
	}
}
