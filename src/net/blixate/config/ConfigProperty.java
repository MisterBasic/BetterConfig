package net.blixate.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import net.blixate.config.error.ConfigTypeException;
import net.blixate.config.parser.NumberParser;
import net.blixate.config.parser.ParseUtils;
import net.blixate.config.parser.StringHelper;
import net.blixate.config.writer.ConfigSerializable;

public class ConfigProperty implements ConfigSerializable{
	
	String name;
	String value;
	
	public ConfigProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public ConfigProperty(Object value) {
		this.value = value.toString();
	}
	
	public ConfigProperty(ConfigSerializable value) {
		// yikes...
		this.value = value.value();
	}
	
	public String getName(){
		return this.name;
	}
	
	public String toString() {
		return name + "=" + value;
	}
	
	public int getAsInt() {
		try {
			return NumberParser.parseInt(value);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			throw new ConfigTypeException("Invalid integer type '" + value + "'");
		}
	}
	
	public BigInteger getAsBigInteger() {
		return new BigInteger(value);
	}
	
	public float getAsFloat() {
		try {
			return (float)getAsDouble();
		}catch(NumberFormatException e) {
			e.printStackTrace();
			throw new ConfigTypeException("Invalid float type '" + value + "'");
		}
	}

	public long getAsLong() {
		try {
			return Long.parseLong(value);
		}catch(NumberFormatException e) {
			throw new ConfigTypeException("Invalid long type '" + value + "'");
		}
	}
	
	public char getAsChar() {
		if(isNumber())
			return (char)getAsInt();
		return getAsString().charAt(0);
	}
	
	public double getAsDouble() {
		try {
			return new BigDecimal(value).doubleValue();
		}catch(NumberFormatException e) {
			throw new ConfigTypeException("Invalid double type '" + value+ "'");
		}
	}
	
	public char[] getAsCharArray() {
		if(isArray()) {
			ArrayList<Character> c = new ArrayList<>();
			for(ConfigProperty prop : asArray().values()) {
				c.add(prop.getAsChar());
			}
			char[] chars = new char[c.size()];
			int i = 0;
			for(Character ch : c) {
				chars[i] = (char)ch;
				i++;
			}
			return chars;
		}
		return getAsString().toCharArray();
	}
	
	public String getAsString() {
		if(isNull()) {
			return null;
		}
		if(isString()) {
			return StringHelper.escape(value.substring(1, value.length()-1));
		}else{
			throw new ConfigTypeException("Invalid string type '" + value + "'");
		}
	}
	
	public boolean isString() {
		return ParseUtils.matches("STRING", this.value);
	}
	
	public boolean isNumber() {
		return ParseUtils.matches("NUMBER", this.value);
	}
	
	public boolean isNull() {
		return ParseUtils.matches("NULL", this.value) || this.value == null;
	}
	
	public boolean isArray() {
		return this instanceof ConfigArray;
	}
	
	public ConfigArray asArray() {
		if(isNull()) {
			return null;
		}
		return (ConfigArray)this;
	}

	public boolean getAsBoolean() {
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			return value.equalsIgnoreCase("true");
		}
		throw new ConfigTypeException("Invalid boolean type '" + value + "'");
	}
	
	@Override
	public String value() {
		if(isString()) {
			return "\"" + this.getAsString() + "\"";
		}
		if(isArray()) { // Just in case!
			return this.asArray().value();
		}
		return this.value;
	}
}
