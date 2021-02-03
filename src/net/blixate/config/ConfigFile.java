package net.blixate.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import net.blixate.config.sections.ConfigDictionary;
import net.blixate.config.sections.ConfigProperty;
import net.blixate.config.sections.ConfigSection;

public class ConfigFile {
	
	private File file;
	private ArrayList<ConfigSection> sections;
	
	public ConfigFile(File f) {
		this.file = f;
		sections = new ArrayList<>();
	}

	/**
	 * This must be called before any other methods.
	 * The important parts of the file will be loaded into memory, the rest
	 * will just be ignored.
	 * @return true if the reading succeeded, false if an error occurred.
	 * @throws IOException
	 */
	public boolean read() throws IOException {
		InputStream stream = this.openReader(); // Use openReader() to create an InputStream
		// A lot of variable definitions...
		int i;
		String token = "";
		boolean inSection = false;
		boolean isKey = true;
		boolean inString = false;
		boolean inComment = false;
		int inArray = 0;
		String key = "";
		ConfigSection section = null;
		String string = "";
		// This stores the properties of an array.
		ArrayList<HashMap<String, ConfigProperty>> arrayProperties = new ArrayList<>();
		// This stores the current name of the dictionary.
		// It's stored in an array, just because.
		ArrayList<String> dictionaryName = new ArrayList<String>();
		int fidx = 0;
		while((i = stream.read()) != -1) {
			System.out.println((char)(i) + ", " + token); // Purely used for debugging purposes.
			if(inComment) {
				if(i == '\n' || i == '\r') { // windows uses \r, which is a bug i've encountered far too often. Just here incase.
					inComment = false;
				}
				token = ""; // Always. Reset. The. Token.
				continue;
			}else{
				if(i == '#' && !inString) { // Defines a comment
					inComment = true;
					token = "";
				}
			}
			if (!inString) // Ignore these characters, unless we are in a string.
				if(i == ' ' || i == '\t' || i == '\n' || i == '\r')
					continue;
			char c = (char)i;
			// '[' and ']' define a section
			if(section == null) { // Check if a section is currently not defined.
				token += c;
				if(c == '[') {
					if(inSection) { // I'm not sure why this is here.
						sections.add(section);
						section = null;
					}
					inSection = true;
					token = "";
				}
				if(c == ']') { // End the section
					if(!inSection) {
						return error("Misplaced ']'", fidx);
					}
					// Creates the section
					// the "section" field could also be stored as an array to allow
					// nested sections?
					inSection = false;
					section = new ConfigSection(token.substring(0, token.length()-1));
					isKey = true;
					token = "";
				}
				
				fidx ++;
				continue;
			}
			else // If there is a section already defined
			{
				if(inString) { // Handle strings before properties
					if(c == '"') {
						inString = false;
						token = string;
						string = "";
						continue;
					}
					string += c;
					continue;
				}
				if(c == '{') { // Handle array starters.
					inArray++;
					isKey = true;
					arrayProperties.add(new HashMap<>());
					dictionaryName.add(key);
					key = "";
					continue;
				}
				switch(c) {
				case '"': // Start a string, the string is ended above.
					inString = true;
					break;
				case '[': // Start a new section.
					inSection = true;
					sections.add(section);
					section = null;
					break;
				case '=':
					if(isKey) {
						isKey = false;
						key = token;
						token = "";
						break;
					}
				case '}':
					if(!key.isEmpty()) {
						arrayProperties.get(arrayProperties.size() - 1).put(key, new ConfigProperty(key, token));
					}
					if(inArray < 1) {
						return error("Mismatched '}'", fidx);
					}
					if(inArray > 1) {
						//TODO: Add support for nested dictionaries.
						//System.out.println("Dictionary inside dictionary!");
					}else {
						section.addDictionary(dictionaryName.remove(dictionaryName.size()-1), new ConfigDictionary(arrayProperties.remove(arrayProperties.size() - 1), null));
					}
					
					token = "";
					key = "";
					isKey = true;
					inArray--;
					break;
				case ',':
					if(inArray > 0) {
						if(!isKey) {
							arrayProperties.get(arrayProperties.size() - 1).put(key, new ConfigProperty(key, token));
							isKey = true;
							key = "";
							token = "";
						}
						else{
							// TODO: Add support for arrays, which do not define a value.
							// Arrays will only define keys, so:
							// { "a", "b", "c" }
							// instead of
							// { a = "a", b = "b", c = "c" }
							return error("This might be how arrays work?", fidx);
						}
					}else{
						return error("Malformed dictionary", fidx); // TODO: More error information.
					}
					break;
				case ';':
					if(!isKey) {
						isKey = true;
						section.addProperty(key, new ConfigProperty(key, token));
						token = "";
						break;
					}
				// Characters that should be ignored.
				case ' ':
				case '\t':
				case '\n':
				case '\r':
					break;
				default: // If there isn't any tokens currently, just keep reading
					token += c;
					fidx ++;
				}
			}
		}
		if(inString) return error("Unmatched string", fidx); // This means that a string wasn't closed.
		if(section != null) {
			sections.add(section); // If we have a section, add it to "sections"
		}
		return true;
	}
	
	public ConfigSection getSection(String name) {
		for(ConfigSection section : sections) {
			if(section.getName().equalsIgnoreCase(name)) {
				return section;
			}
		}
		return null;
	}
	
	public ConfigSection[] getSections() {
		return this.sections.toArray(new ConfigSection[0]);
	}
	
	private boolean error(String s, int index) {
		// Should probably use a Logger, but System.err is just more familiar.
		System.err.println("[CONFIG ERROR] " + s + " (at " + index + ")");
		return false;
	}
	
	// These 2 methods shouldn't really need to be public, but they are here just in case.
	public InputStream openReader() throws IOException {
		if(!this.file.exists())
			this.file.createNewFile();
		return Files.newInputStream(this.file.toPath());
	}
	
	public OutputStream openWriter() throws IOException {
		if(!this.file.exists())
			this.file.createNewFile();
		return Files.newOutputStream(this.file.toPath());
	}
	
}
