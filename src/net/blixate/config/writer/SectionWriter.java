package net.blixate.config.writer;

import java.util.Stack;

/**
 * @deprecated This is inefficent, uses too much memory, and was a terrible solution.
 * Please use the updated {@link net.blixate.config.writer.v2.ConfigWriter} instead.
 */
@Deprecated
public class SectionWriter extends WriterObject{
	String name;
	Stack<PropertyWriter> properties;
	
	SectionWriter(String name) {
		this.name = name;
		this.parent = null;
		this.properties = new Stack<>();
	}
	/* Returns entire section */
	public String write() {
		String s = "[" + this.name + "]\n";
		for(PropertyWriter prop : properties) {
			s += prop.write() + "\n";
		}
		return s;
	}
}
