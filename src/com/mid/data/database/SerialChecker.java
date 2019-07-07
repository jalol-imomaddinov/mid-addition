package com.mid.data.database;

import com.mid.util.CodeStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialChecker {

    private final static String serial = "BBC1250X452C";

    public static boolean checkSerial() {

	File file = new File(Paths.DATA_PATH, Paths.SERIAL_FILE);

        System.out.println(file);
        
	if (!file.exists()) {
	    return false;
	}

	try {
	    FileInputStream fis = new FileInputStream(file);
	    DataInputStream dis = new DataInputStream(fis);

	    String ser = CodeStream.encode(dis);

	    dis.close();
	    fis.close();

	    if (ser.equals(serial)) {
		return true;
	    }
	}
	catch (FileNotFoundException ex) {
	}
	catch (IOException ex) {
	}

	return false;
    }

    public static void saveSerial() {
	File path = new File(Paths.DATA_PATH);
	path.mkdirs();

	File file = new File(path, Paths.SERIAL_FILE);

	try {
	    file.createNewFile();
	    FileOutputStream fos = new FileOutputStream(file);
	    DataOutputStream dos = new DataOutputStream(fos);

	    CodeStream.decode(dos, serial);

	    dos.close();
	    fos.close();
	}
	catch (IOException ex) {
	}
    }

    public static boolean validSerial(String value) {
	if (value.equals(serial)) {
	    return true;
	}
	return false;
    }
}
