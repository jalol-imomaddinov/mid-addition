package com.mid.ui.workers.add;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mid.data.common.JobType;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.WorkerDAO;
import com.mid.data.model.WorkerModel;
import com.mid.ui.control.ControlUtil;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.ui.error.ErrorLog;
import com.mid.ui.workers.WorkersController;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class WorkerAddController implements Initializable {

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXComboBox<JobType> jobCombo;
    @FXML
    private JFXCheckBox firedCheck;

    private WorkerModel workerModel;
    private WorkersController workersController;

    private boolean save;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	jobCombo.getItems().addAll(JobType.values());
	jobCombo.getSelectionModel().select(0);
    }

    public void newModel() {
	this.workerModel = new WorkerModel();
	save = true;
	clearEntries();
    }

    public void setWorkersController(WorkersController workersController) {
	this.workersController = workersController;
    }

    public void loadModel(WorkerModel model) {
	this.workerModel = model;
	this.nameField.setText(model.getName());
	this.phoneField.setText(model.getPhone());
	this.jobCombo.getSelectionModel().select(model.getJobType());
	this.firedCheck.setSelected(model.isFired());
	save = false;
    }

    @FXML
    private void saveAction(ActionEvent event) {
	try {
	    String name = ControlUtil.getFieldString(nameField);
	    String phone = ControlUtil.getFieldString(phoneField);
	    boolean fired = firedCheck.isSelected();
	    JobType jobType = jobCombo.getValue();

	    WorkerDAO wdao = DataAccessHelper.getDataAccessHelper().getWorkerDAO();
	    if (save) {
		wdao.addWorker(name, phone, jobType, fired);
	    }
	    else {
		workerModel.setFired(fired);
		workerModel.setJobType(jobType);
		workerModel.setPhone(phone);
		workerModel.setName(name);

		wdao.updateWorker(workerModel.getId(), name, phone, jobType, fired);
		workersController.refreshWorkers();
	    }

	    closeStage();
	}
	catch (IllegalFieldValueException ex) {
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void cancelAction(ActionEvent event) {
	closeStage();
    }

    private void closeStage() {
	Stage stage = (Stage) nameField.getScene().getWindow();
	stage.close();
    }

    private void clearEntries() {
	nameField.setText("");
	phoneField.setText("");
	firedCheck.setSelected(false);
	jobCombo.getSelectionModel().select(0);
    }
}
