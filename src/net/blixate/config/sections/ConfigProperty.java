package net.blixate.config.sections;

public class ConfigProperty {
	
	// This defines a property
	// String properties are funny, since "42" will still parse
	// as 42 if getAsInt() is called.
	
	String name;
	String value;
	
	public ConfigProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public int getAsInt() {
		try {
			return Integer.parseInt(value);
		}catch(NumberFormatException e) {
			return 0;
		}
	}
	
	public float getAsFloat() {
		try {
			return Float.parseFloat(value);
		}catch(NumberFormatException e) {
			return 0f;
		}
	}
	
	public long getAsLong() {
		try {
			return Long.parseLong(value);
		}catch(NumberFormatException e) {
			return (long)0;
		}
	}
	
	public char getAsChar() {
		try {
			return value.charAt(0);
		}catch(IndexOutOfBoundsException e) {
			return 0;
		}
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
		return value;
	}
	
	public String getName() {
		return this.name;
	}

	public boolean getAsBoolean() {
		if(value.equalsIgnoreCase("true")) {
			return true;
		}else if(value.equalsIgnoreCase("false")){
			return false;
		}
		return false;
	}
}
