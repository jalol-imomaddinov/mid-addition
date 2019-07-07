package com.mid.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author
 */
public class CodeStream {

    public static void decode(DataOutput output, String str) throws IOException {

	int len = str.length();

	output.writeInt(len);

	for (int i = 0; i < len; i++) {
	    output.writeChar(str.charAt(i) + 7);
	}
    }

    public static String encode(DataInput input) throws IOException {

	int len = input.readInt();
	char[] chars = new char[len];

	for (int i = 0; i < len; i++) {
	    chars[i] = (char) (input.readChar() - 7);
	}
	return new String(chars);
    }
}
