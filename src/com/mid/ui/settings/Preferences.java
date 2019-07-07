package com.mid.ui.settings;

/**
 * @author
 */
public final class Preferences {

    public static final String CONFIG_FILE = "config.dat";

    private static final Preferences preferences = new Preferences();

    public static Preferences getPreferences() {
	return preferences;
    }

    private int tableItemCount = 15;

    private String serial = "";
    private String password = "";
    private String username = "";

    private Preferences() {
    }

    public boolean validSerial(String serial) {
	return isEqual(this.serial, serial);
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public boolean validPassword(String password) {
	return isEqual(this.password, password);
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getUsername() {
	return username;
    }

    public boolean validUsername(String username) {
	return isEqual(this.username, username);
    }

    public void setTableItemCount(int tableItemCount) {
	this.tableItemCount = tableItemCount;
    }

    public int getTableItemCount() {
	return tableItemCount;
    }

    public void initConfig() {
    }

    private boolean isEqual(String v1, String v2) {
	return v1.toLowerCase().equals(v2.toLowerCase());
    }
}
