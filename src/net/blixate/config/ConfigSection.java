package net.blixate.config;

import java.util.HashMap;

public class ConfigSection {
	
	String name;
	String parentSectionName;
	// HashMaps are used for convenience.
	// Although HashMaps are probably not necessary; I know how to use them, so why not?
	HashMap<String, ConfigProperty> properties;
	
	ConfigSection(String name) {
		this.name = name;
		properties = new HashMap<>();
		//dictionaries = new HashMap<>();
	}
	
	public ConfigSection(String name, String parent) {
		this(name);
		this.parentSectionName = parent;
	}
	
	public void addProperty(String name, ConfigProperty value) {
		properties.put(name, value);
	}
	
	public void addProperty(ConfigProperty value) {
		properties.put(value.getName(), value);
	}
	
	public boolean hasProperty(String name) {
		return this.properties.containsKey(name);
	}
	
	public ConfigProperty getProperty(String name) {
		ConfigProperty prop = this.properties.get(name);
		if(prop == null) {
			return null;
		}
		return prop;
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
	
	public boolean hasParent() {
		return this.parentSectionName != null;
	}
	
	public String getParentName() {
		return this.parentSectionName;
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
	// Since this is separate, you can have a property with the
	// same name as a dictionary property.
	//public ConfigDictionary getDictionary(String key) {
	//	return dictionaries.get(key);
	//}
}
