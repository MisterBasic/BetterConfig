package net.blixate.config;

public class ConfigArray extends ConfigProperty {
	
	ConfigProperty[] value;
	
	ConfigArray(String name, ConfigProperty[] value) {
		super(name, "");
		this.value = value;
	}
	
	public void add(ConfigProperty value) {
		ConfigProperty[] name;
		if(this.value == null) {
			this.value = new ConfigProperty[0];
		}
		name = new ConfigProperty[this.value.length + 1];
		System.arraycopy(this.value, 0, name, 0, this.value.length);
		name[this.value.length] = value;
		this.value = name;
	}
	
	public ConfigProperty get(int index) {
		if(value == null) {
			throw new RuntimeException("Array not initalized!");
		}
		if(index >= value.length) {
			throw new ArrayIndexOutOfBoundsException("Array does not have property at index " + index);
		}
		return this.value[index];
	}
	
	public int size() {
		return this.value.length;
	}
	
	public ConfigProperty[] values() {
		return this.value;
	}
	
	public String toString() {
		if(this.value == null || this.value.length == 0) {
			return this.name + "={}";
		}
		StringBuilder sb = new StringBuilder("{");
		for(ConfigProperty val : this.value) {
			sb.append(val.getName());
			sb.append(", ");
		}
		return this.name + "=" + sb.toString().substring(0, sb.length()-2) + "}";
	}
	
}
