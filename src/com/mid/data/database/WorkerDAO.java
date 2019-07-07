package com.mid.data.database;

import com.mid.data.common.JobType;
import com.mid.data.model.TotalModel;
import com.mid.data.model.WorkerModel;
import com.mid.ui.control.IllegalFieldValueException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * @author
 */
public interface WorkerDAO {

    public List<Integer> bindedWorkers(int id) throws SQLException;

    public ObservableList<WorkerModel> getWorkers() throws SQLException;

    public void bindWorker(int worker_id, int id) throws SQLException;

    public void unbindWorkers(int id) throws SQLException;

    public void removePaychack(int pid) throws SQLException;

    public void removeSendedPaychacks(int pid) throws SQLException;

    public void addPaychack(int order_id, int wid, int type, double amound, String note, LocalDate date) throws SQLException;

    public ObservableList<TotalModel> getPaychacks(int worker_id, LocalDate stDate, LocalDate edDate) throws SQLException;

    public ObservableList<TotalModel> getPaychacks() throws SQLException;

    public void addWorker(String name, String phone, JobType jobType, boolean fired) throws SQLException;

    public void updateWorker(int wid, String name, String phone, JobType jobType, boolean fired) throws SQLException;

    public WorkerModel getWorkerById(int id);
}
