package net.blixate.config;

import net.blixate.config.parser.Token;

public class ConfigProperty {
	
	// This defines a property
	// String properties are funny, since "42" will still parse
	// as 42 if getAsInt() is called.
	
	String name;
	String value;
	
	ConfigProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String toString() {
		return name + "=" + value;
	}
	
	public int getAsInt() {
		return Integer.parseInt(value);
	}
	
	public float getAsFloat() {
		return Float.parseFloat(value);
	}
	
	public long getAsLong() {
		return Long.parseLong(value);
	}
	
	public char getAsChar() {
		return getAsString().charAt(0);
	}
	
	public double getAsDouble() {
		try {
			return Double.parseDouble(value);
		}catch(NumberFormatException e) {
			return 0d;
		}
	}
	
	public char[] getAsCharArray() {
		return getAsString().toCharArray();
	}
	
	public String getAsString() {
		if(isString()) {
			return value.substring(1, value.length()-1);
		}else{
			return value; // ??
		}
	}
	
	public boolean isString() {
		return Token.STRING.match(this.value);
	}
	
	public boolean isNumber() {
		return Token.NUMBER.match(this.value);
	}
	
	public boolean isNull() {
		return Token.NULL.match(this.value);
	}
	
	public ConfigArray asArray() {
		return (ConfigArray)this;
	}

	public boolean getAsBoolean() {
		if(value.equalsIgnoreCase("true")) {
			return true;
		}else if(value.equalsIgnoreCase("false")){
			return false;
		}
		return false;
	}
	
	/* Should pretty much never be used. */
	public String rawVal() {
		return this.value;
	}
}
