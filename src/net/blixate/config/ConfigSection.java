package net.blixate.config;

import java.util.HashMap;

public class ConfigSection {
	
	String name;
	HashMap<String, ConfigProperty> properties;
	
	public ConfigSection(String name) {
		this.name = name;
		properties = new HashMap<>();
	}
	
	public void addProperty(String name, ConfigProperty value) {
		properties.put(name, value);
	}
	
	public void addProperty(ConfigProperty value) {
		// There is no checks for if it exists ON PURPOSE!
		// This is how ConfigWriter V2 works.
		properties.put(value.getName(), value);
	}
	
	public boolean hasProperty(String name) {
		return this.properties.containsKey(name);
	}
	
	public ConfigProperty getProperty(String name) {
		return hasProperty(name) ? this.properties.get(name) : new ConfigNull(name);
	}
	
	public ConfigProperty[] getProperties() {
		return properties.values().toArray(new ConfigProperty[0]);
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "[" + this.name + ", propertyCount=" + properties.size() + "]";
	}
	
	/* A bunch of getters for different types of data.
	 * This is to help clean up code, as sometimes lines can be a bit too long.
	 */
	
	public int getInt(String property) {
		return getProperty(property).getAsInt();
	}
	
	public float getFloat(String property) {
		return getProperty(property).getAsFloat();
	}
	
	public String getString(String property) {
		return getProperty(property).getAsString();
	}
	
	public double getDouble(String property) {
		return getProperty(property).getAsDouble();
	}
	
	public long getLong(String property) {
		return getProperty(property).getAsLong();
	}
	
	public ConfigArray getArray(String property) {
		return getProperty(property).asArray();
	}

	public boolean getBoolean(String property) {
		return getProperty(property).getAsBoolean();
	}
}
