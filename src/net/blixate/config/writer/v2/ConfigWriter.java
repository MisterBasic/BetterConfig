package net.blixate.config.writer.v2;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import net.blixate.config.ConfigArray;
import net.blixate.config.ConfigFile;
import net.blixate.config.ConfigNull;
import net.blixate.config.ConfigProperty;
import net.blixate.config.ConfigSection;
import net.blixate.config.writer.ConfigSerializable;

/**
 * Allows editing of {@link ConfigFile}s without removing any properties.
 * It also reduces memory costs (Unlike {@link net.blixate.config.writer.v2.ConfigFileWriter}) by keeping all data in
 * one class, not multiple.
 * 
 * Improved {@code set} methods allow any type to be stored!
 * 
 * @see ConfigFile
 */
public class ConfigWriter {
	
	public static ConfigWriter getWriter(ConfigFile file) {
		ConfigWriter writer = new ConfigWriter(file);
		writer.load();
		return writer;
	}
	
	ArrayList<ConfigSection> sections;
	ConfigFile file;
	
	private ConfigWriter(ConfigFile file) {
		this.file = file;
		this.sections = new ArrayList<>();
	}
	
	public void save(OutputStream stream) throws IOException {
		for(ConfigSection section : sections) {
			if(section.getProperties().length == 0) {
				continue;
			}
			if(!section.getName().equals(ConfigFile.GLOBAL_SECTION)) {
				stream.write(writeSection(section).getBytes());
			}
			for(ConfigProperty prop : section.getProperties()) {
				stream.write(writeProperty(prop).getBytes());
			}
		}
		file.read(); // Reload the file so our new data is immediately accessible.
	}
	
	private void load() {
		for(ConfigSection section : this.file.getSections()) {
			sections.add(section);
		}
	}
	
	public String writeSection(ConfigSection section) {
		return "[" + section.getName() + "]\n";
	}
	
	public String writeProperty(ConfigProperty prop) {
		return prop.getName() + " = " + prop.value() + "\n";
	}
	
	public <T> void set(String section, String propertyName, T[] value) {
		if(value == null) {
			set(section, new ConfigNull(propertyName));
		}
		set(section, ConfigArray.toConfigArray(propertyName, value));
	}
	
	public void set(String section, String propertyName, boolean value) {
		set(section, new ConfigProperty(propertyName, ""+value));
	}
	public void set(String section, String propertyName, float value) {
		set(section, new ConfigProperty(propertyName, ""+value));
	}
	public void set(String section, String propertyName, double value) {
		set(section, new ConfigProperty(propertyName, ""+value));
	}
	public void set(String section, String propertyName, long value) {
		set(section, new ConfigProperty(propertyName, ""+value));
	}
	public void set(String section, String propertyName, byte value) {
		set(section, new ConfigProperty(propertyName, ""+value));
	}
	public void set(String section, String propertyName, short value) {
		set(section, new ConfigProperty(propertyName, ""+value));
	}
	public void set(String section, String propertyName, char value) {
		set(section, new ConfigProperty(propertyName, ""+value));
	}
	public void set(String section, String propertyName, String value) {
		set(section, new ConfigProperty(propertyName, "\"" + value + "\""));
	}
	public void set(String section, String propertyName, ConfigSerializable value) {
		set(section, new ConfigProperty(propertyName, value.value()));
	}
	
	private void set(String section, ConfigProperty prop) {
		ConfigSection sec = getSection(section);
		if(sec == null) {
			sec = new ConfigSection(section);
			sections.add(sec);
		}
		sec.addProperty(prop);
	}
	
	private ConfigSection getSection(String name) {
		for(int i = 0; i < sections.size() ; i++) {
			if(sections.get(i).getName().equals(name))
				return sections.get(i);
		}
		return null;
	}

	public OutputStream getFileStream() throws IOException {
		return Files.newOutputStream(file.getFile().toPath());
	}
	
}
