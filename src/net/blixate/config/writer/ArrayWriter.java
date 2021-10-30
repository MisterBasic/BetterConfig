package net.blixate.config.writer;

public class ArrayWriter extends PropertyWriter {
	Object[] values;
	ArrayWriter(String name, Object[] value, WriterObject parent) { 
		super(name, null, parent);
		this.name = name; this.values = value; 
		this.parent = parent;
	}

	@Override
	public String write() {
		String valueString = "{";
		for(Object v : values) {
			if(v instanceof String)
				valueString += "\"" + v.toString() + "\", ";
			else if(v instanceof ConfigSerializable)
				valueString += ((ConfigSerializable)v).value();
			else
				valueString += v.toString() + ", ";
		}
		return this.name + " = " + valueString.substring(0, valueString.length()-2) + "};";
	}
}
