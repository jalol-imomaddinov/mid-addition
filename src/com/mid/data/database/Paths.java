package com.mid.data.database;

import java.io.File;

public class Paths {

    public static final String APP_PATH = new File("").getAbsolutePath();
    public static final String SRC_PATH = System.getenv("APPDATA") + "/MIData/";
    public static final String DATA_PATH = SRC_PATH + "data/";
    public static final String BACKUP_PATH = SRC_PATH + "backups/";
    public static final String EXT_PATH = SRC_PATH + "";
    public static final String SERIAL_FILE = "0x01fff.bm";
    public static final String FIRST_RUN_FILE = DATA_PATH + ".firstrun";

    public static final String CATALOG_DATA_NAME = "products.db3";
    public static final String DATA_NAME = "data.db3";

    public static final String SUCCESS_NAME = "data/import_success";
    public static final String IMAGE_NAME = "img/";

    public static final String CATALOG_DATABASE = SRC_PATH + CATALOG_DATA_NAME;
    public static final String DATA_DATABASE = SRC_PATH + DATA_NAME;
    public static final String SUCCESS = SRC_PATH + SUCCESS_NAME;

    public static final String EMPTY_BASE = APP_PATH + "/resources/emptybase.mbp";
    public static final String PDF_EXPORTER = APP_PATH + "/PDFExporter.exe";
    public static final String LOG_PATH = APP_PATH + "/log/";
    
    public static final String IMAGE_PATH = SRC_PATH + IMAGE_NAME;

    public static void createPaths() {
	new File(SRC_PATH).mkdirs();
	new File(DATA_PATH).mkdirs();
	new File(EXT_PATH).mkdirs();
	new File(IMAGE_PATH).mkdirs();
	new File(LOG_PATH).mkdirs();
    }

    public static String getExporterPath() {
	String path = new File("").getAbsolutePath() + "\\PDFExporter.exe";
	System.out.println(path);
	return path;
    }
}
