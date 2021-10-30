package net.blixate.config;

import java.math.BigInteger;

import net.blixate.config.error.ConfigTypeException;
import net.blixate.config.parser.NumberParser;
import net.blixate.config.parser.Token;
import net.blixate.config.writer.ConfigSerializable;

public class ConfigProperty implements ConfigSerializable{
	
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
			return Float.parseFloat(value);
		}catch(NumberFormatException e) {
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
		return getAsString().charAt(0);
	}
	
	public double getAsDouble() {
		try {
			return Double.parseDouble(value);
		}catch(NumberFormatException e) {
			throw new ConfigTypeException("Invalid double type '" + value+ "'");
		}
	}
	
	public char[] getAsCharArray() {
		return getAsString().toCharArray();
	}
	
	public String getAsString() {
		if(isString()) {
			return value.substring(1, value.length()-1).replace("\\\"", "\"");
		}else{
			throw new ConfigTypeException("Invalid string type '" + value + "'");
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
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			return value.equalsIgnoreCase("true");
		}
		throw new ConfigTypeException("Invalid boolean type '" + value + "'");
	}
	
	@Override
	public String value() {
		if(isString()) {
			return this.getAsString();
		}
		return this.value;
	}
}
