package com.mid.data.database;

import java.sql.SQLException;

/**
 * @author
 */
public interface DynamicDAO {

    public String getTheme() throws SQLException;

    public void setTheme(String name) throws SQLException;

    public String getMobile() throws SQLException;

    public void setMobile(String mobile) throws SQLException;

    public String getHome() throws SQLException;

    public void setHome(String number) throws SQLException;
}
