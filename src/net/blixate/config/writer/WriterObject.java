package net.blixate.config.writer;

/**
 * @deprecated This is inefficent, uses too much memory, and was a terrible solution.
 * Please use the updated {@link net.blixate.config.writer.v2.ConfigWriter} instead.
 */
@Deprecated
public class WriterObject {
	String name;
	WriterObject parent;
	public String write() { return null; }
}
