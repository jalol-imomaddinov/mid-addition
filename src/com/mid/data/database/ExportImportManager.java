/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mid.data.database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author User
 */
public class ExportImportManager {

    protected ExportImportManager() {}
    
    public static void doExport(File path) throws IOException {
        path.createNewFile();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(path));
        writeFile(dos, Paths.CATALOG_DATABASE, Paths.CATALOG_DATA_NAME);
        writeFile(dos, Paths.DATA_DATABASE, Paths.DATA_NAME);
        
        String[] imgList = new File(Paths.IMAGE_PATH).list((File dir, String name) -> {
            return name.endsWith(".jpg");
        });
        
        dos.writeInt(imgList.length);
        
        for (String img : imgList) {
            writeFile(dos, Paths.IMAGE_PATH + img, img);
        }
        dos.close();
    }
    
    /**
     * int len
     * string chars
     * long 
     */
    
    private static void writeFile(DataOutputStream stream, String file, String name) throws FileNotFoundException, IOException {
        System.out.println("writeFIle: " + file + " > " + name);
        FileInputStream fis = new FileInputStream(new File(file));
        stream.writeInt(name.length());
        stream.writeChars(name);
        stream.writeLong(fis.available());
        byte[] buffer = new byte[1024];
        while(fis.read(buffer) != -1) {
            stream.write(buffer);
        }
        fis.close();
    }
    
    public static void doImport(File path) throws FileNotFoundException, IOException {
        System.out.println(path);
        DataInputStream dis = new DataInputStream(new FileInputStream(path));
        readFile(dis, Paths.SRC_PATH); //catalog
        readFile(dis, Paths.SRC_PATH); //data
        
        int count = dis.readInt();
        
        for (int i = 0; i < count; i++) {
            readFile(dis, Paths.SRC_PATH + Paths.IMAGE_NAME);
        }
        dis.close();
    }
    
    private static void readFile(DataInputStream stream, String outPath)throws IOException {
        int nameLen = stream.readInt();
        char[] chs = new char[nameLen];
        for (int i = 0; i < nameLen; i++) {
            chs[i] = stream.readChar();
        }
        String name = new String(chs);
        
        File outFile = new File(outPath + name);
        outFile.getParentFile().mkdirs();
        outFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(outFile);
        long len = stream.readLong();
        System.out.println("out: " + outFile);
        for (long i = 0; i < len; i++) fos.write(stream.read());
        fos.close();
    }
}