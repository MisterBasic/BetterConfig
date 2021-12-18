package net.blixate.config.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Stack;

import net.blixate.config.ConfigFile;

/**
 * @deprecated This is inefficent, uses too much memory, and was a terrible solution.
 * Please use the updated {@link net.blixate.config.writer.v2.ConfigWriter} instead.
 */
@Deprecated
public class ConfigFileWriter {
	// TODO: Rewrite this holy shit.
	File file;
	
	Stack<SectionWriter> sections;
	
	public ConfigFileWriter(File f) {
		this.file = f;
		this.sections = new Stack<>();
	}
	
	public void setSection(String name) {
		sections.push(new SectionWriter(name));
	}
	public void writeProperty(String name, String value) {
		sections.peek().properties.add(new PropertyWriter(name, "\"" + value + "\"", sections.peek()));
	}
	public void writeProperty(String name, Object... value) {
		sections.peek().properties.add(new ArrayWriter(name, value, sections.peek()));
	}
	/** Fixes a small issue with arrays being considered objects. */
	public void writeArray(String name, Object[] value) {
		sections.peek().properties.add(new ArrayWriter(name, value, sections.peek()));
	}
	public void writeProperty(String name, int value) {
		sections.peek().properties.add(new PropertyWriter(name, String.valueOf(value), sections.peek()));
	}
	public void writeProperty(String name, ConfigSerializable value) {
		if(value == null) {
			this.writeProperty(name, "null");
			return;
		}
		sections.peek().properties.add(new PropertyWriter(name, value.value(), sections.peek()));
	}
	public void writeProperty(String name, Object value) {
		sections.peek().properties.add(new PropertyWriter(name, String.valueOf(value), sections.peek()));
	}
	
	/** Close and save the file */
	public void save() throws IOException {
		OutputStream stream = openWriter();
		for(SectionWriter sec : sections) {
			try{
				stream.write(sec.write().getBytes());
			}catch(Throwable t) {
				System.err.println("An error occurred while saving section " + sec.name + ":");
				t.printStackTrace();
			}
		}
		stream.close();
	}
	
	private OutputStream openWriter() throws IOException {
		if(!this.file.exists())
			this.file.createNewFile();
		return Files.newOutputStream(this.file.toPath());
	}

	public static ConfigFileWriter loadFile(ConfigFile cfg) {
		ConfigFileWriter writer = new ConfigFileWriter(cfg.getFile());
		return writer;
	}
}
