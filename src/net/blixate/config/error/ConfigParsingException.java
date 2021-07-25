package net.blixate.config.error;

public class ConfigParsingException extends RuntimeException {
	public ConfigParsingException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -8145353177611783807L;
}
