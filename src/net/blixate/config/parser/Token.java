package net.blixate.config.parser;

import java.util.regex.Pattern;

public enum Token {
	// Regexes that match the desired tokens.
	COMMENT("\\#.*"),
	NULL("null"),
	BOOLEAN("(true|false)"),
	SECTION("\\[[a-zA-Z_]+[a-zA-Z0-9_\\.]*\\]"),
	PROPERTY("[a-zA-Z_][a-zA-Z0-9_\\.]*"),
	EQUALS("[=:]"),
	STRING("\".*\"", "\'.*\'"),
	NUMBER("[+-]?0x[0-9A-Fa-f]+", "[+-]?0b[01]", "[+-]?0o[0-7]", "[+-]?[0-9]*[.]?[0-9]+"),
	LBRACE("\\{"),
	RBRACE("\\}"),
	LPAREN("\\("),
	RPAREN("\\)"),
	COMMA(","),
	ENDL(";");
	
	private Pattern regex;
	
	Token(String regex) {
		this.regex = Pattern.compile(regex);
	}
	Token(String...regexes) {
		String[] s = new String[regexes.length];
		for(int i = 0; i < regexes.length; i++) s[i] = "(" + regexes[i] + ")";
		this.regex = Pattern.compile(String.join("|", s));
	}
	Token(String regex, int...flags) {
		int flag = 0;
		for(int f : flags) {
			flag |= f;
		}
		this.regex = Pattern.compile(regex, flag);
	}

	public String regex() {
		return regex.pattern();
	}
	
	public boolean match(String test) {
		return this.regex.matcher(test).matches();
	}
}
