package com.mid.util;

import java.math.BigInteger;

/**
 * @author
 */
public class StringUtil {

    public static String toAmound(double v) {
	String str = BigInteger.valueOf((int) v).toString();
	char[] chs = str.toCharArray();
	int len = chs.length;
	// 
	int count = len / 3;

	char[] newchs = new char[len + count];

	int j = newchs.length - 1;
	int threes = 0;
	char ch;

	for (int i = 0; (i < len) && (j > -1); i++, j--) {
	    ch = chs[len - i - 1];
	    newchs[j] = ch;
	    threes++;
	    if (threes == 3) {
		j--;
		if (j == 1) {
		    if (chs[0] != '-') {
			newchs[j] = ',';
		    }
		}
		else if (j != 0) {
		    newchs[j] = ',';
		}
		threes = 0;
	    }
	}

	return new String(newchs);
    }

    public static double fromAmound(String value) {
	String newValue = value.trim();
	if (newValue.length() == 0) {
	    return 0;
	}

	StringBuilder builder = new StringBuilder();

	for (int i = 0; i < newValue.length(); i++) {
	    char ch = newValue.charAt(i);
	    if (ch != ',') {
		builder.append(ch);
	    }
	}
	return Double.parseDouble(builder.toString());
    }

    public static String DoubletoString(double v) {
	long i = (long) v;
	String str;
	if (v - i != 0) {
	    str = Double.toString(v);
	}
	else {
	    str = Long.toString(i);
	    return str;
	}

	StringBuilder sb = new StringBuilder();

	int plotIndex = str.indexOf('.');
	sb.append(str, 0, plotIndex);

	if ((v - i) != 0) {
	    if (plotIndex != str.length()) {
		sb.append('.');
		sb.append(str.charAt(plotIndex + 1));
		if (str.length() > plotIndex + 2) {
		    sb.append(str.charAt(plotIndex + 2));
		}
	    }
	}

	return sb.toString();
    }
}
