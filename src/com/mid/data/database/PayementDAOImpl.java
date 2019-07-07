package com.mid.data.database;

import com.mid.data.model.PayementModel;
import com.mid.util.DateUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayementDAOImpl implements PayementDAO {

    @Override
    public List<PayementModel> getPayements(int contract_id) throws SQLException {
	List<PayementModel> models = new ArrayList<>();

	PreparedStatement ps = DatabaseHandler.getDataConnection().prepareStatement("select * from Payements where order_id = ?;");
	ps.setInt(1, contract_id);

	ResultSet rs = ps.executeQuery();

	while (rs.next()) {
	    PayementModel model = new PayementModel();
	    model.setId(rs.getInt(1));
	    model.setAmound(rs.getInt(3));
	    model.setDate(DateUtil.dbParse(rs.getString(4)));

	    models.add(model);
	}

	rs.close();
	ps.close();

	return models;
    }

    @Override
    public void addPayement(int id, LocalDate date, double amound) throws SQLException {
	PreparedStatement ps = DatabaseHandler.getDataConnection().prepareStatement("insert into Payements(order_id, amound, date) values(?, ?, ?)");
	ps.setInt(1, id);
	ps.setDouble(2, amound);
	ps.setString(3, DateUtil.dbFormat(date));

	ps.execute();
	ps.close();
	
	ContractDAO contractDAO = DataAccessHelper.getDataAccessHelper().getContractDAO();
	contractDAO.refreshAmoundInfo();
    }

    @Override
    public void removePayement(int id) throws SQLException {
//	System.out.println("remove " + id);
	PreparedStatement ps = DatabaseHandler.getDataConnection().prepareStatement("delete from Payements where payement_id = ?;");
	ps.setInt(1, id);

	ps.execute();
	ps.close();
	
	ContractDAO contractDAO = DataAccessHelper.getDataAccessHelper().getContractDAO();
	contractDAO.refreshAmoundInfo();
    }
}
