package net.blixate.config.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Stack;

public class ConfigFileWriter {
	class WriterObject {
		String name;
		WriterObject parent;
		public String write() { return null; }
	}
	class PropertyWriter extends WriterObject {
		String name;
		String value;
		PropertyWriter(String name, String value, WriterObject parent) { 
			this.name = name; this.value = value; 
			this.parent = parent;
		}
		public String write() {
			return this.name + " = " + this.value + ";";
		}
	}
	class ArrayWriter extends WriterObject {
		String name;
		String[] values;
		ArrayWriter(String name, String[] value, WriterObject parent) { 
			this.name = name; this.values = value; 
			this.parent = parent;
		}
		public String write() {
			String valueString = "{";
			for(String v : values)
				valueString += v + ", ";
			return this.name + " = " + valueString.substring(0, valueString.length()-2) + "};";
		}
	}
	class SectionWriter extends WriterObject {
		String name;
		Stack<PropertyWriter> properties;
		Stack<ArrayWriter> arrays;
		
		SectionWriter(String name) {
			this.name = name;
			this.parent = null;
			this.properties = new Stack<>();
			this.arrays = new Stack<>();
		}
		/* Returns entire section */
		public String write() {
			String s = "[" + this.name + "]\n";
			for(PropertyWriter prop : properties) {
				s += prop.write() + "\n";
			}
			for(ArrayWriter prop : arrays) {
				s += prop.write() + "\n";
			}
			return s;
		}
	}
	
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
		sections.peek().properties.add(new PropertyWriter(name, value, sections.peek()));
	}
	public void writeArray(String name, String[] values) {
		sections.peek().arrays.add(new ArrayWriter(name, values, sections.peek()));
	}
	
	public void save() throws IOException {
		OutputStream stream = openWriter();
		for(SectionWriter sec : sections) {
			stream.write(sec.write().getBytes());
		}
		stream.close();
	}
	
	// These 2 methods shouldn't really need to be public, but they are here just in case.
	private OutputStream openWriter() throws IOException {
		if(!this.file.exists())
			this.file.createNewFile();
		return Files.newOutputStream(this.file.toPath());
	}
	
}
