package com.mid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class IOUtil {

    private static final List<Thread> threads = new ArrayList<>();

    public static void fullWriteInto(InputStream input, OutputStream output) throws IOException {
	byte[] buffer = new byte[10240];
	while (input.read(buffer) > -1) {
	    output.write(buffer);
	}
    }

    public static void writeInto(InputStream input, OutputStream output, int avialable) throws IOException {
	byte[] bytes = new byte[avialable];
	input.read(bytes);
	output.write(bytes);
    }

    public static void copy(String input, String output) throws FileNotFoundException, IOException {

	File file = new File(input);
	Path path = file.toPath();

	FileOutputStream fos = new FileOutputStream(output);
	Files.copy(path, fos);
    }

    private static class CopyThread extends Thread {

	private final String input, output;

	public CopyThread(String input, String output) {
	    this.input = input;
	    this.output = output;
	}

	@Override
	public void run() {

	    File inputFile = new File(input);

	    if (!inputFile.exists()) {
	    }

	    File outputFile = new File(output);

	    try {

		FileInputStream fis = new FileInputStream(inputFile);

		if (outputFile.exists()) {
		    outputFile.delete();
		    outputFile.createNewFile();
		}

		try (FileOutputStream fos = new FileOutputStream(outputFile)) {
		    byte[] buffer = new byte[1024];
		    while (fis.read(buffer) > -1) {
			fos.write(buffer);
		    }

		    fos.flush();
		}

		threads.remove(this);

	    }
	    catch (FileNotFoundException ex) {
	    }
	    catch (IOException ex) {
	    }
	}
    }
}
