package net.blixate.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigFile {
	
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
	
	public ConfigSection getSection(String name) {
		for(ConfigSection section : sections) {
			if(section.getName().toUpperCase().equals(name.toUpperCase())) {
				return section;
			}
		}
		return null;
	}
	
	public ConfigSection[] getSectionChildren(String name) {
		ArrayList<ConfigSection> sections = new ArrayList<>();
		for(ConfigSection section : this.sections) {
			if(section.getParentName().equals(name)) {
				sections.add(section);
			}
		}
		return sections.toArray(new ConfigSection[0]);
	}
	
	public ConfigSection[] getSections() {
		return this.sections;
	}
	
	// For reading the file data
	// -------------------------------
	
	public static ConfigFile readFile(String name) {
		return readFile(name, null);
	}
	
	public static ConfigFile readFile(String name, OutputStream out) {
		File f = new File(name);
		ConfigFile c = new ConfigFile(f, out);
		try{
			c.read();
		} catch (IOException e) {
			return null;
		}
		return c;
	}
	
	/**
	 * Load, lex then parse.
	 * @returns true if the reading succeeded, false if an error occurred.
	 * @throws IOException
	 */
	public void read() throws IOException {
		String s = load(); // read file
		ConfigParser parser = new ConfigParser(this.dump);
		parser.lex(s); // lexical analysis
		parser.parse(); // parsing
		this.sections = parser.getSections();
	}
	
	/**
	 * Read the file and return the content.
	 * @returns A string containing the content of the file
	 * @throws IOException
	 */
	private String load() throws IOException{
		InputStream stream = this.openReader();
		String s = "";
		while(stream.available() != 0) s += (char)stream.read();
		stream.close();
		return s;
	}
	
	/**
	 * Create an InputStream for the file.
	 * @returns InputStream of file
	 * @throws IOException
	 */
	private InputStream openReader() throws IOException {
		if(!this.file.exists())
			this.file.createNewFile();
		return Files.newInputStream(this.file.toPath());
	}
	
}
