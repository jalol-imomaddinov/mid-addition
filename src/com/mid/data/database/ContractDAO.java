package com.mid.data.database;

import com.mid.data.common.AmoundInfo;
import com.mid.data.common.StateType;
import com.mid.data.model.FullContractModel;
import com.mid.data.model.SimpleContractModel;
import java.sql.SQLException;

/**
 * @author
 */
public interface ContractDAO {

    public void initContractReader(int current, int max) throws SQLException;

    public SimpleContractModel readSimpleContract(SimpleContractModel model) throws SQLException;

    public FullContractModel readFullContract(int id, FullContractModel model) throws SQLException;

    public void updateContract(FullContractModel model) throws SQLException;

    public void saveContract(FullContractModel model) throws SQLException;

    public void removeContract(SimpleContractModel model) throws SQLException;
    
    public void setCriteria(String criteria);

    public void refreshAmoundInfo() throws SQLException;
    
    public AmoundInfo getAmoundInfo() throws SQLException;
    
    public int getContractCount() throws SQLException;

    public void updateFused(SimpleContractModel aThis, Boolean newValue) throws SQLException;

    public void updateState(SimpleContractModel aThis, StateType newValue) throws SQLException;

    public int nextContractNumber() throws SQLException;

    public void refreshSimpleContract(SimpleContractModel contractModel) throws SQLException;

    public double[] getWorkersPay(int id) throws SQLException;
}
