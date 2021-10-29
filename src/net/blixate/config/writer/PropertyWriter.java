package net.blixate.config.writer;

public class PropertyWriter extends WriterObject {
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
