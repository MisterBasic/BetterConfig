package net.blixate.config.util;
/**
 * <p>Convert one class to another<p>
 * F is FROM<br>
 * T is TO
 */
public interface Converter<F, T> {
	public T convert(F input);
}
