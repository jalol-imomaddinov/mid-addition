package com.mid.data.database;

import com.mid.data.model.PayementModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface PayementDAO {

    public List<PayementModel> getPayements(int contract_id) throws SQLException;

    public void addPayement(int id, LocalDate date, double amound) throws SQLException;

    public void removePayement(int id) throws SQLException;
}
