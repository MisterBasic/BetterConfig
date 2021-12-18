package net.blixate.config.parser;

public class StringHelper {
	public static String escape(String s) {
		return s
				.replace("\\\"", "\"")
				.replace("\\n", "\n")
				.replace("\\t", "\t")
				.replace("\\r", "\r")
				.replace("\\'", "'")
				.replace("\\\\", "\\")
				.replace("\\b", "\b")
				.replace("\\0", "\0");
	}
	
	/* \b probably doesn't need to be escaped */
	public static String unescape(String s) {
		return s
				.replace("\"", "\\\"")
				.replace("\n", "\\n")
				.replace("\t", "\\t")
				.replace("\r", "\\r")
				.replace("\'", "\\'")
				.replace("\\", "\\\\")
				.replace("\b", "\\b")
				.replace("\0", "\\0");
	}
}
