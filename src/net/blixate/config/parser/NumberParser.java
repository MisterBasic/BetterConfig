package net.blixate.config.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberParser {
	public static final String[] prefixes = { "0x", "0b", "0o" };
	public static final int[] prefixRadix = { 16, 2, 8 };
	
	public static String getParsable(String value) {
		String pValue;
		int offset = value.charAt(0) == '-' ? 1 : 0;
		boolean isNegative = offset == 1;
		if(value.length() > offset + 1) {
			if(isPrefix(value.substring(offset, offset+2)))
				offset+=2;
		}
		pValue = value.substring(offset);
		return (isNegative ? "-" : "" ) + pValue;
	}
	
	public static int getRadix(String value) {
		// we don't want to parse the minus sign.
		int offset = value.charAt(0) == '-' ? 1 : 0;
		if(value.length() > offset+1) {
			int pIdx = prefixIdx(value.substring(offset, offset+2));
			if(pIdx >= 0) {
				return prefixRadix[pIdx];
			}
		}
		return 10; // return default radix
	}
	
	private static boolean isPrefix(String prefix) {
		return prefixIdx(prefix) != -1;
	}
	
	private static int prefixIdx(String prefix) {
		for(int i = 0; i < prefixes.length; i++) {
			if(prefix.equals(prefixes[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public static int parseInt(String value) {
		int radix = getRadix(value);
		String parseString = getParsable(value);
		return Integer.parseInt(parseString, radix);
	}
	
	/* These are going to change, just a temporary solution. */
	public BigInteger parseBigInt(String value) {
		return new BigInteger(value);
	}
	
	public BigDecimal parseBigNumber(String value) {
		return new BigDecimal(value);
	}
}
