package net.blixate.config;

import java.util.ArrayList;

import net.blixate.config.util.Converter;
import net.blixate.config.writer.ConfigSerializable;

public class ConfigArray extends ConfigProperty implements ConfigSerializable {
	
	ConfigProperty[] value;
	
	public ConfigArray(String name, ConfigProperty[] value) {
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
			sb.append(val.value);
			sb.append(", ");
		}
		return this.name + "=" + sb.toString().substring(0, sb.length()-2) + "}";
	}
	
	public String value() {
		String valueString = "{";
		for(ConfigProperty v : value) {
			if(v == null) {
				valueString += "null,";
			}
			else if(v instanceof ConfigSerializable)
				valueString += ((ConfigSerializable) v).value();
			else 
				valueString += v;
			
			valueString += ", ";
		}
		valueString = (valueString.length() > 2 ? valueString.substring(0, valueString.length()-2) : "{") + "}";
		return valueString;
	}
	
	@Override
	public boolean isArray() {
		return true;
	}

	public String[] toStringArray() {
		return toArray(new String[0], new Converter<ConfigProperty, String>() {
			public String convert(ConfigProperty input) {
				return input.getAsString();
			}
		});
	}
	
	public Integer[] toIntegerArray() {
		return toArray(new Integer[0], new Converter<ConfigProperty, Integer>() {
			public Integer convert(ConfigProperty input) {
				return input.getAsInt();
			}
		});
	}
	
	public Float[] toFloatArray() {
		return toArray(new Float[0], new Converter<ConfigProperty, Float>() {
			public Float convert(ConfigProperty input) {
				return input.getAsFloat();
			}
		});
	}
	
	public <T> T[] toArray(T[] array, Converter<ConfigProperty, T> converter) {
		ArrayList<T> obj = new ArrayList<>();
		if(this.isNull()) {
			return array;
		}
		for(ConfigProperty prop : this.value) {
			obj.add(converter.convert(prop));
		}
		return (T[])obj.toArray(array);
	}
	
	public static <T> ConfigProperty toConfigArray(String name, T[] objs) {
		ArrayList<ConfigProperty> prop = new ArrayList<>();
		for(T obj : objs) {
			if(obj instanceof ConfigSerializable) {
				prop.add(new ConfigProperty(((ConfigSerializable) obj).value()));
			}else {
				prop.add(new ConfigProperty(String.valueOf(obj)));
			}
		}
		return new ConfigArray(name, prop.toArray(new ConfigProperty[0]));
	}
}
