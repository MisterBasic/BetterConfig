package net.blixate.config;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import net.blixate.config.parser.ConfigParser;
import net.blixate.config.writer.v2.ConfigWriter;

public class ConfigFile {
	
	public static final String GLOBAL_SECTION = "Global";
	
	private File file;
	private ConfigSection[] sections;
	OutputStream dump = null;
	
	public ConfigFile(String fileName) {
		this(new File(fileName));
	}
	
	public ConfigFile(File input) {
		this.file = input;
	}
	
	public ConfigFile(File input, OutputStream dump) {
		this(input);
		this.dump = dump;
	}

	public ConfigProperty property(String section, String property) {
		return this.getSection(section).getProperty(property);
	}
	
	public ConfigProperty globalProperty(String property) {
		if(!hasSection(GLOBAL_SECTION) || this.getSection(GLOBAL_SECTION).getProperties().length == 0) return null;
		return this.getSection(GLOBAL_SECTION).getProperty(property);
	}
	
	public boolean hasGlobalProperty(String name) {
		return this.globalProperty(name) != null;
	}
	
	public boolean hasSection(String name) {
		return this.getSection(name) != null;
	}
	
	public ConfigSection getSection(String name) {
		for(ConfigSection section : sections) {
			if(section.getName().toUpperCase().equals(name.toUpperCase())) {
				return section;
			}
		}
		return null;
	}
	
	public ConfigSection[] getSections() {
		return this.sections;
	}
	
	// For reading the file data
	// -------------------------------
	
	/**
	 * Read, lex and parse a configuration file.
	 * @param name The name of the file
	 */
	public static ConfigFile readFile(String name) {
		return readFile(name, null);
	}
	
	/**
	 * Read, lex and parse a configuration file. Allows debug information to be printed.
	 * @param name The name of the file
	 * @param debugStream where to output debug information.
	 */
	public static ConfigFile readFile(String name, OutputStream debugStream) {
		File f = new File(name);
		ConfigFile c = new ConfigFile(f, debugStream);
		try{
			c.read();
		} catch (IOException e) {
			return null;
		}
		return c;
	}
	
	/**
	 * Load, lex then parse.
	 * This function can be called multiple times to update our memory object with the state of the file.
	 * @throws IOException
	 */
	public ConfigFile read() throws IOException {
		String s = load(); // read file
		ConfigParser parser = new ConfigParser(this.dump);
		parser.lex(s); // lexical analysis
		parser.parse(); // parsing
		this.sections = parser.getSections();
		return this;
	}
	
	/**
	 * Read the file and return the content.
	 * @returns A string containing the content of the file
	 * @throws IOException
	 */
	private String load() throws IOException{
		String content = "";
		for(String line : Files.readAllLines(this.file.toPath())) {
			content += line + "\n";
		}
		return content;
	}

	// For writing file data
	
	public File getFile() {
		return this.file;
	}
	
	public ConfigWriter getWriter() {
		return ConfigWriter.getWriter(this);
	}
	
}
