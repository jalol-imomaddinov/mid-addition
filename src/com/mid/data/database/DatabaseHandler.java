package com.mid.data.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.Function;

public final class DatabaseHandler {

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    private static DatabaseHandler handler = null;

    private static Connection catalogConn = null;
    private static Connection dataConn = null;

    static {
	createConnection();
    }

    private DatabaseHandler() {
    }

    public static DatabaseHandler getInstance() {
	if (handler == null) {
	    handler = new DatabaseHandler();
	}
	return handler;
    }

    private static void createConnection() {
	try {
	    Class.forName("org.sqlite.JDBC");
	    catalogConn = DriverManager.getConnection("jdbc:sqlite:" + Paths.CATALOG_DATABASE);
	    dataConn = DriverManager.getConnection("jdbc:sqlite:" + Paths.DATA_DATABASE);

            System.out.println(Paths.CATALOG_DATABASE);
            System.out.println(Paths.DATA_DATABASE);
            
	    Function hasMatch = new Function() {
		@Override
		protected void xFunc() throws SQLException {
		    if (args() != 2) {
			throw new SQLException("agument mismatch for function hasMatch(a, b)");
		    }
		    String str = super.value_text(0);
		    String substr = super.value_text(1);
		    boolean contains = str.toLowerCase().contains(substr.toLowerCase());
		    result(contains ? 1 : 0);
		}
	    };
	    
	    Function.create(dataConn, "hasMatch", hasMatch);
	    Function.create(catalogConn, "hasMatch", hasMatch);
	}
	catch (ClassNotFoundException | SQLException e) {
	    JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
	    System.exit(0);
	}
    }

    public static void main(String[] args) throws Exception {
	DatabaseHandler.getInstance();
    }

    public static Connection getCatalogConnection() {
	return catalogConn;
    }

    public static Connection getDataConnection() {
	return dataConn;
    }

    public static void closeConnections() throws SQLException {
	catalogConn.close();
	dataConn.close();
    }
}
