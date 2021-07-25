package net.blixate.config.parser;

public enum Token {
	COMMENT("\\#.*"),
	NULL("null"),
	BOOLEAN("(true|false)"),
	SECTION("\\[[a-zA-Z_]+[a-zA-Z0-9_]*(.*?[:].*?[a-zA-Z_]+[a-zA-Z0-9_]*)?\\]"),
	PROPERTY("[*]?[a-zA-Z_][a-zA-Z0-9_]*"),
	EQUALS("[=]"),
	STRING("\".*?\"", "\'.*?\'"),
	NUMBER("[0-9]*[.]?[0-9]+"),
	LBRACE("\\{"),
	RBRACE("\\}"),
	LPAREN("\\("),
	RPAREN("\\)"),
	COMMA(","),
	ENDL(";");
	
	private String regex;
	
	Token(String regex) {
		this.regex = regex;
	}
	Token(String...regexes) {
		// ()|()
		String[] s = new String[regexes.length];
		for(int i = 0; i < regexes.length; i++) s[i] = "(" + regexes[i] + ")";
		this.regex = String.join("|", s);
	}

	public String regex() {
		return regex;
	}
	
	public boolean match(String test) {
		return test.matches(this.regex);
	}
}
