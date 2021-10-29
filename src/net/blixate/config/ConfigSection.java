package net.blixate.config;

import java.util.HashMap;

public class ConfigSection {
	
	String name;
	String parentSectionName;
	HashMap<String, ConfigProperty> properties;
	
	ConfigSection(String name) {
		this.name = name;
		properties = new HashMap<>();
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
	
	public String toString() {
		if(hasParent()) {
			return "[" + this.name + ", propertyCount=" + properties.size() + "]";
		}
		return "[" + this.name + ", propertyCount=" + properties.size() + ", parent=" + this.getParentName() + "]";
	}
}
