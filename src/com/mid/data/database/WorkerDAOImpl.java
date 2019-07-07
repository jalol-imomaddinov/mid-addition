package com.mid.data.database;

import com.mid.data.common.JobType;
import com.mid.data.model.TotalModel;
import com.mid.data.model.WorkerModel;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.util.DateUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author
 */
public class WorkerDAOImpl implements WorkerDAO {

    private final static String BINDETS = "SELECT worker_id from BindedWorkers where order_id = ?;";

    private final ObservableList<WorkerModel> WorkerModels = FXCollections.observableArrayList();
    private final ObservableList<TotalModel> totalModels = FXCollections.observableArrayList();

    @Override
    public ObservableList<WorkerModel> getWorkers() throws SQLException {
	if (WorkerModels.isEmpty()) {
	    initWorkers();
	}
	return WorkerModels;
    }

    @Override
    public ObservableList<TotalModel> getPaychacks() throws SQLException {
	return totalModels;
    }

    private void initWorkers() throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("select * from Workers");

	ResultSet executeQuery = statement.executeQuery();

	WorkerModels.clear();

	while (executeQuery.next()) {
	    WorkerModel model = new WorkerModel();

	    model.setId(executeQuery.getInt(1));
	    model.setName(executeQuery.getString(2));

	    int job = executeQuery.getInt(3);
	    model.setJobType(JobType.jobById(job));

	    model.setFired(executeQuery.getInt(4) > 0);
	    model.setBinded(false);
            model.setPhone(executeQuery.getString(5));

	    WorkerModels.add(model);
	}

	executeQuery.close();
	statement.close();
    }

    @Override
    public List<Integer> bindedWorkers(int id) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement(BINDETS);
	statement.setInt(1, id);

	ResultSet executeQuery = statement.executeQuery();
	List<Integer> worker_ids = new ArrayList<>();

	while (executeQuery.next()) {
	    int worker_id = executeQuery.getInt(1);
	    worker_ids.add(worker_id);
	}

	executeQuery.close();
	statement.close();

//	System.out.println(id + ":" + worker_ids.toString());

	return worker_ids;
    }

    @Override
    public void bindWorker(int worker_id, int id) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("insert into BindedWorkers(order_id, worker_id) values(?, ?);");
	statement.setInt(1, id);
	statement.setInt(2, worker_id);
	statement.execute();
	statement.close();
    }

    @Override
    public void unbindWorkers(int id) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("delete from BindedWorkers where order_id = ?;");
	statement.setInt(1, id);
	statement.execute();

	statement.close();
    }

    @Override
    public void removePaychack(int pid) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("delete from Total where total_id = ?;");
	statement.setInt(1, pid);
	statement.execute();

	statement.close();
    }

    @Override
    public void removeSendedPaychacks(int order_id) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("delete from Total where order_id = ?;");
	statement.setInt(1, order_id);
	statement.execute();

	statement.close();
    }

    @Override
    public void addPaychack(int orider_id, int wid, int type, double amound, String note, LocalDate date) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("insert into Total(order_id, worker_id, type, amound, note, date) values(?, ?, ?, ?, ?, ?);");
	statement.setInt(1, orider_id);
	statement.setInt(2, wid);
	statement.setInt(3, type);
	statement.setDouble(4, amound);
	statement.setString(5, note);
	statement.setString(6, DateUtil.dbFormat(date));
	statement.execute();
	statement.close();
    }

    @Override
    public ObservableList<TotalModel> getPaychacks(int wid, LocalDate stDate, LocalDate edDate) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("select * from Total where (date >= ? and date <= ?) and worker_id = ?;");
	statement.setString(1, DateUtil.dbFormat(stDate));
	statement.setString(2, DateUtil.dbFormat(edDate));
	statement.setInt(3, wid);
	ResultSet result = statement.executeQuery();

	totalModels.clear();

	while (result.next()) {
	    TotalModel model = new TotalModel();
	    model.setId(result.getInt(1));
	    model.setWorkerId(result.getInt(2));
	    model.setAmound(result.getDouble(3));
	    model.setDate(DateUtil.dbParse(result.getString(4)));
	    model.setNote(result.getString(5));
	    model.setType(result.getInt(6));

	    totalModels.add(model);
	}

	result.close();
	statement.close();

	return totalModels;
    }

    @Override
    public void addWorker(String name, String phone, JobType jobType, boolean fired) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("insert into Workers(name, job, fired, phone) values(?, ?, ?, ?);");
	statement.setString(1, name);
	statement.setInt(2, jobType.getId());
	statement.setInt(3, fired ? 1 : 0);
	statement.setString(4, phone);
	statement.execute();
	statement.close();

	initWorkers();
    }

    @Override
    public void updateWorker(int wid, String name, String phone, JobType jobType, boolean fired) throws SQLException {
	PreparedStatement statement = DatabaseHandler.getDataConnection().prepareStatement("update Workers set name = ?, job = ?, fired = ?, phone = ? where worker_id = ?;");
	statement.setString(1, name);
	statement.setInt(2, jobType.getId());
	statement.setInt(3, fired ? 1 : 0);
	statement.setString(4, phone);
	statement.setInt(4, wid);
	statement.execute();
	statement.close();
    }

    @Override
    public WorkerModel getWorkerById(int id) {
        for (WorkerModel model : WorkerModels) {
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }
}
