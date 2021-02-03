package net.blixate.config.sections;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigDictionary {
	
	/*
	 * This defines a dictionary.
	 */
	
	String name;
	HashMap<String, ConfigProperty> properties;
	HashMap<String, ConfigDictionary> internalDictionaries; // Pretty useless field for now.

	public ConfigDictionary(HashMap<String, ConfigProperty> properties, HashMap<String, ConfigDictionary> dicts) {
		this.properties = properties;
		this.internalDictionaries = dicts;
	}
	
	public String[] keys() {
		ArrayList<String> st = new ArrayList<>();
		for(String s : properties.keySet())
			st.add(s);
		if(internalDictionaries != null)
			for(String s : internalDictionaries.keySet())
				st.add(s);
		return st.toArray(new String[0]);
	}
	
	public ConfigProperty[] properties() {
		ArrayList<ConfigProperty> st = new ArrayList<>();
		for(ConfigProperty s : properties.values())
			st.add(s);
		return st.toArray(new ConfigProperty[0]);
	}
	
	public ConfigProperty getProperty(String name) {
		return properties.get(name);
	}
	
	public ConfigDictionary getDictionary(String name) {
		return internalDictionaries.get(name);
	}
	
}
