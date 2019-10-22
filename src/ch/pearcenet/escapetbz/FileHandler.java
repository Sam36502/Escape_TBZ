package ch.pearcenet.escapetbz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import ch.pearcenet.escapetbz.exceptions.InvalidPropException;

public class FileHandler {
	
	public enum Lvl {
		INFO,
		WARNING,
		ERROR
	}
	
	private String filename;
	private HashMap<String, String> content;
	private String logName;
	private Lvl reportLvl;
	
	/// CONSTRUCTORS ///
	
	public FileHandler(String filename) {
		super();
		this.filename = filename;
		this.logName = null;
		this.content = new HashMap<>();
	}
	
	public FileHandler(String filename, String logName, Lvl ReportLvl) {
		super();
		this.filename = filename;
		this.logName = logName;
		this.reportLvl = ReportLvl;
		this.content = new HashMap<>();
		log(Lvl.INFO, "Filehandler Initialized and Logging is enabled.");
	}
	
	/// GETTERS ///
	
	public String getFilename() {
		return filename;
	}
	
	public boolean hasProp(String key) {
		return content.containsKey(key);
	}
	
	public String getProp(String key) {
		if (content.containsKey(key)) {
			return content.get(key);
		} else {
			log(Lvl.WARNING, "A non existent property key was requested.");
			throw new InvalidPropException("Key '" + key + "' does not exist.");
		}
	}
	
	public int getPropInt(String key) {
		try {
			return Integer.parseInt(getProp(key));
		} catch (NumberFormatException e) {
			log(Lvl.WARNING, "'" + key + "' was requested as an integer, but isn't an integer.");
			throw new InvalidPropException("Key '" + key + "' does not refer to an integer.");
		}
	}
	
	/// PUBLIC METHODS ///
	
	public void log(Lvl level, String msg) {
		if (logName != null && level.ordinal() >= reportLvl.ordinal()) {
			System.out.println("[" + level + "] " + logName + ": " + msg);
		}
		
		if (level == Lvl.ERROR) {
			System.exit(1);
		}
	}
	
	public void loadFile() {
		FileInputStream fis = null;
		Scanner file = null;
		
		// Open file
		try {
			fis = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			log(Lvl.ERROR, "Failed to open file '" + filename + "'. File not found.");
		}
		file = new Scanner(fis);
		
		// Parse file contents
		while (file.hasNextLine()) {
			String line = file.nextLine();
			int cidx = line.indexOf('#');
			
			// Remove any comments from the current line
			if (cidx != -1) {
				line = line.substring(0, cidx);
			}
			
			// Parse line
			String[] half = line.split("=");
			if (half.length >= 2) {
				String key = half[0].trim();
				String value = half[1].trim();
				
				// Insert data into HashMap
				content.put(key, value);
			}
		}
		
		// Close file
		file.close();
		try {
			fis.close();
		} catch (IOException e) {
			log(Lvl.WARNING, "Failed to close file '" + filename + "'. Resource leak present.");
		}
	}
	
}
