package com.mid.data.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DynamicDAOImpl implements DynamicDAO {

    @Override
    public String getTheme() throws SQLException {
	Statement stmt = DatabaseHandler.getDataConnection().createStatement();
	ResultSet result = stmt.executeQuery("select theme from DynamicData;");

	String theme = result.getString(1);
	result.close();
	stmt.close();

	return theme;
    }

    @Override
    public void setTheme(String name) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getDataConnection().prepareStatement("update DynamicData set theme = ?");
	stmt.setString(1, name);
	stmt.execute();

	stmt.close();
    }

    @Override
    public String getMobile() throws SQLException {
	Statement stmt = DatabaseHandler.getDataConnection().createStatement();
	ResultSet result = stmt.executeQuery("select mobile from DynamicData;");
	String number = result.getString(1);
	result.close();
	stmt.close();

	return number;
    }

    @Override
    public void setMobile(String mobile) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getDataConnection().prepareStatement("update DynamicData set mobile = ?");
	stmt.setString(1, mobile);
	stmt.execute();
	stmt.close();
    }

    @Override
    public String getHome() throws SQLException {
	Statement stmt = DatabaseHandler.getDataConnection().createStatement();
	ResultSet result = stmt.executeQuery("select home from DynamicData;");
	String number = result.getString(1);
	result.close();
	stmt.close();

	return number;
    }

    @Override
    public void setHome(String number) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getDataConnection().prepareStatement("update DynamicData set home = ?");
	stmt.setString(1, number);
	stmt.execute();
	stmt.close();
    }
}
