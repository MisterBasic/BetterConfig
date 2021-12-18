package net.blixate.config.writer;

/**
 * @deprecated This is inefficent, uses too much memory, and was a terrible solution.
 * Please use the updated {@link net.blixate.config.writer.v2.ConfigWriter} instead.
 */
@Deprecated
public class PropertyWriter extends WriterObject {
	String name;
	String value;
	PropertyWriter(String name, String value, WriterObject parent) { 
		this.name = name; this.value = value; 
		this.parent = parent;
	}
	public String write() {
		if(this.value == null) this.value = "null";
		return this.name + " = " + this.value;
	}
}
