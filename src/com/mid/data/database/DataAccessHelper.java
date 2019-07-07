package com.mid.data.database;

import com.mid.ui.error.ErrorLog;
import java.sql.SQLException;

public class DataAccessHelper {

    private final ContractDAO contractDAO;
    private final ProductDAO catalogDAO;
    private final WorkerDAO workerDAO;
    private final PayementDAO payementDAO;
    private final DynamicDAO dynamicDAO;

    private boolean isReaded = false;

    public DataAccessHelper() {
	this.contractDAO = new ContractDAOImpl();
	this.catalogDAO = new ProductDAOImpl();
	this.workerDAO = new WorkerDAOImpl();
	this.payementDAO = new PayementDAOImpl();
	this.dynamicDAO = new DynamicDAOImpl();
    }

    public ContractDAO getContractDAO() {
	return contractDAO;
    }

    public ProductDAO getCatalogDAO() {
	return catalogDAO;
    }

    public WorkerDAO getWorkerDAO() {
	return workerDAO;
    }

    public PayementDAO getPayementDAO() {
	return payementDAO;
    }

    public DynamicDAO getDynamicDAO() {
	return dynamicDAO;
    }

    public void read() throws SQLException {
        workerDAO.getWorkers();
    }

    private static final DataAccessHelper dataAccessHelper = new DataAccessHelper();

    public static DataAccessHelper getDataAccessHelper() {
	try {
	    dataAccessHelper.read();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
	return dataAccessHelper;
    }
}
