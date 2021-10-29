package net.blixate.config.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Stack;

public class ConfigFileWriter {
	
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
	public void writeProperty(String name, Object value) {
		sections.peek().properties.add(new PropertyWriter(name, String.valueOf(value), sections.peek()));
	}
	public void writeProperty(String name, int value) {
		sections.peek().properties.add(new PropertyWriter(name, String.valueOf(value), sections.peek()));
	}
	
	/** Close and save the file */
	public void save() throws IOException {
		OutputStream stream = openWriter();
		for(SectionWriter sec : sections) {
			stream.write(sec.write().getBytes());
		}
		stream.close();
	}
	
	private OutputStream openWriter() throws IOException {
		if(!this.file.exists())
			this.file.createNewFile();
		return Files.newOutputStream(this.file.toPath());
	}
	
}
