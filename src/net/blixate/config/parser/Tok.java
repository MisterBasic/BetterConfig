package net.blixate.config.parser;

import net.blixate.config.parser.Token;

public class Tok {
	public Token t;
	String data;
	int index;
	
	public Tok(Token t, String data, int start) {
		this.t = t;
		this.data = data;
		this.index = start;
	}
	
	public String toString() {
		return this.t.name() + "=\"" + this.data + "\" at " + this.index;
	}
	
	public String data() {
		return this.data;
	}
	
	public int index() {
		return this.index;
	}
}
