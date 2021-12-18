package net.blixate.config.writer;

/**
 * @deprecated This is inefficent, uses too much memory, and was a terrible solution.
 * Please use the updated {@link net.blixate.config.writer.v2.ConfigWriter} instead.
 */
@Deprecated
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
			if(v == null) {
				valueString += "null,";
			}
			if(v instanceof ConfigSerializable)
				valueString += ((ConfigSerializable) v).value();
			else if(v instanceof String)
				valueString += "\"" + v + "\"";
			else 
				valueString += v;
			
			valueString += ", ";
		}
		valueString = (valueString.length() > 2 ? valueString.substring(0, valueString.length()-2) : "{") + "}";
		return this.name + " = " + valueString;
	}
}
