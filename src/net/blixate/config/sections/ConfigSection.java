package net.blixate.config.sections;

import java.util.HashMap;

public class ConfigSection {
	String name;
	
	// HashMaps are used for convenience.
	// Although HashMaps are probably not necessary; I know how to use them, so why not?
	HashMap<String, ConfigProperty> properties;
	HashMap<String, ConfigDictionary> dictionaries;
	
	public ConfigSection(String name) {
		this.name = name;
		properties = new HashMap<>();
		dictionaries = new HashMap<>();
	}
	
	public void addProperty(String name, ConfigProperty value) {
		properties.put(name, value);
	}
	
	public void addDictionary(String name, ConfigDictionary dict) {
		dictionaries.put(name, dict);
	}
	
	public ConfigProperty getProperty(String name) {
		return this.properties.get(name);
	}
	
	public ConfigProperty[] getProperties() {
		ConfigProperty[] props = new ConfigProperty[properties.size()];
		int i = 0;
		for(ConfigProperty p : properties.values()) {
			props[i] = p;
			i++;
		}
		return props;
	}
	
	public String getName() {
		return name;
	}
	
	// Overrides the default toString()
	// Seems pretty useless tbh
	public String toString() {
		return "[" + this.name + ", propertyCount=" + properties.size() + "]";
	}
	
	// TODO: Allow getProperty() to also get dictionaries.
	// Since this is seperate, you can have a property with the
	// same name as a dictionary property.
	public ConfigDictionary getDictionary(String key) {
		return dictionaries.get(key);
	}
}
