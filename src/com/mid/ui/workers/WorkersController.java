package com.mid.ui.workers;

import com.jfoenix.controls.JFXDatePicker;
import com.mid.data.common.JobType;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.WorkerDAO;
import com.mid.data.model.TotalModel;
import com.mid.data.model.WorkerModel;
import com.mid.ui.control.ControlUtil;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.ui.error.ErrorLog;
import com.mid.ui.workers.add.WorkerAddController;
import com.mid.ui.workers.pay.WorkerPayController;
import com.mid.util.MIDUtil;
import com.mid.util.StringUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class WorkersController implements Initializable {

    @FXML
    private TableView<WorkerModel> workerTable;
    @FXML
    private TableColumn<WorkerModel, String> workerNameCol;
    @FXML
    private TableColumn<WorkerModel, String> workerPhoneCol;
    @FXML
    private TableColumn<WorkerModel, JobType> workerJobCol;
    @FXML
    private TableColumn<WorkerModel, String> workerStateCol;

    @FXML
    private Label nameLabel;

    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXDatePicker endDate;

    @FXML
    private TableView<TotalModel> totalTable;
    @FXML
    private TableColumn<TotalModel, String> totalNoteCol;
    @FXML
    private TableColumn<TotalModel, String> totalDateCol;
    @FXML
    private TableColumn<TotalModel, String> totalAmoundCol;

    @FXML
    private Label amoundLabel;
    @FXML
    private Label payLabel;
    @FXML
    private Label remaindLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	workerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    if (newValue != null) {
		updateWorkerData(newValue);
	    }
	});

	makeRowFactory();

	workerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
	workerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
	workerJobCol.setCellValueFactory(new PropertyValueFactory<>("jobType"));
	workerStateCol.setCellValueFactory(new PropertyValueFactory<>("state"));

	totalNoteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
	totalDateCol.setCellValueFactory(new PropertyValueFactory<>("dateStr"));
	totalAmoundCol.setCellValueFactory(new PropertyValueFactory<>("amoundStr"));

	WorkerDAO wdao = DataAccessHelper.getDataAccessHelper().getWorkerDAO();

	try {
	    workerTable.setItems(wdao.getWorkers());
	    totalTable.setItems(wdao.getPaychacks());
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}

	ChangeListener<LocalDate> changeListener = (observable, oldValue, newValue) -> {
	    if (newValue != null) {
		calculate();
	    }
	};

	startDate.valueProperty().addListener(changeListener);
	endDate.valueProperty().addListener(changeListener);

	updateDates();
    }

    public void updateDates() {
	LocalDate now = LocalDate.now();

	startDate.setValue(LocalDate.of(now.getYear(), now.getMonth(), 1));
	endDate.setValue(LocalDate.of(now.getYear(), now.getMonth(), now.lengthOfMonth()));
    }

    private void updateWorkerData(WorkerModel model) {
	nameLabel.setText(model.getName());
	calculate();
	totalTable.refresh();
    }

    @FXML
    private void addWorkerAction(ActionEvent event) {
	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/workers/add/worker-add.fxml"), "Add Worker", false, null);
	WorkerAddController wac = (WorkerAddController) container.getController();
	wac.setWorkersController(this);
	wac.newModel();
	container.getStage().show();
    }

    @FXML
    private void editWorkerAction(ActionEvent event) {
	WorkerModel worker = workerTable.getSelectionModel().getSelectedItem();
	if (worker != null) {
	    WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/workers/add/worker-add.fxml"), "Edit Worker", false, null);
	    WorkerAddController wac = (WorkerAddController) container.getController();
	    wac.setWorkersController(this);
	    wac.loadModel(worker);
	    container.getStage().show();
	}
    }

    @FXML
    private void addPayAction(ActionEvent event) {
	WorkerModel model = workerTable.getSelectionModel().getSelectedItem();

	if (model != null) {
	    WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/workers/pay/worker-pay.fxml"), "Add Pay", false, null);
	    WorkerPayController wpc = (WorkerPayController) container.getController();
	    wpc.setWorkersController(this);
	    wpc.setModel(model);
	    container.getStage().show();
	}
    }

    @FXML
    private void deletePayAction(ActionEvent event) {
	TotalModel item = totalTable.getSelectionModel().getSelectedItem();
	if (item != null) {
	    try {
		WorkerDAO wdao = DataAccessHelper.getDataAccessHelper().getWorkerDAO();
		wdao.removePaychack(item.getId());
		calculate();
		refreshPayements();
	    }
	    catch (SQLException ex) {
		ErrorLog.getErrorLog().show(ex);
	    }
	}
    }

    public void calculate() {
	try {
	    WorkerModel worker = workerTable.getSelectionModel().getSelectedItem();
	    if (worker != null) {
		LocalDate stDate = ControlUtil.getFieldDate(startDate);
		LocalDate edDate = ControlUtil.getFieldDate(endDate);

		WorkerDAO wdao = DataAccessHelper.getDataAccessHelper().getWorkerDAO();
		ObservableList<TotalModel> paychacks = wdao.getPaychacks(worker.getId(), stDate, edDate);

		double amound = 0;
		double pay = 0;
		double remaind = 0;

		for (TotalModel paychack : paychacks) {
		    paychack.init();
		    if (paychack.getType() == 0) {
			pay += paychack.getAmound();
		    }
		    else if (paychack.getType() == 1) {
			amound += paychack.getAmound();
		    }
		}

		remaind = amound - pay;

		amoundLabel.setText(StringUtil.toAmound(amound));
		payLabel.setText(StringUtil.toAmound(pay));
		remaindLabel.setText(StringUtil.toAmound(remaind));
	    }
	}
	catch (IllegalFieldValueException ex) {
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    private void makeRowFactory() {

	totalTable.setRowFactory(param -> {
	    return new TableRow<TotalModel>() {
		@Override
		protected void updateItem(TotalModel item, boolean empty) {
		    super.updateItem(item, empty);

		    if (!empty && item != null) {

			getStyleClass().remove("row-state-complete");
			getStyleClass().remove("row-state-lating");

			if (item.getType() == 0) {
			    getStyleClass().add("row-state-complete");
			}
			else {
			    getStyleClass().add("row-state-lating");
			}
		    }
		}
	    };
	});
    }

    public void refreshWorkers() {
	workerTable.refresh();
    }

    public void refreshPayements() {
	totalTable.refresh();
    }
}
