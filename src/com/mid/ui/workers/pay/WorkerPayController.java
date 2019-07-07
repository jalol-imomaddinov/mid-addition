package com.mid.ui.workers.pay;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.WorkerDAO;
import com.mid.data.model.WorkerModel;
import com.mid.ui.control.ControlUtil;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.ui.control.converter.AmoundConverter;
import com.mid.ui.error.ErrorLog;
import com.mid.ui.workers.WorkersController;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class WorkerPayController implements Initializable {

    @FXML
    private JFXRadioButton workRadio;
    @FXML
    private JFXRadioButton payRadio;

    @FXML
    private Label nameLabel;
    @FXML
    private JFXTextField noteField;
    @FXML
    private JFXTextField amoundField;

    private WorkersController workersController;

    private WorkerModel model;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	ToggleGroup group = new ToggleGroup();
	group.getToggles().addAll(workRadio, payRadio);
	amoundField.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));
    }

    public void setModel(WorkerModel model) {
	this.model = model;
	nameLabel.setText(model.getName());
	noteField.setText("");
	amoundField.setText("");
    }

    public void setWorkersController(WorkersController workersController) {
	this.workersController = workersController;
    }

    @FXML
    private void saveAction(ActionEvent event) {
	try {
	    String name = ControlUtil.getFieldString(noteField);
	    double amound = ControlUtil.getFieldAmound(amoundField);

	    int type = 0;

	    if (workRadio.isSelected()) {
		type = 1;
	    }
	    else if (payRadio.isSelected()) {
		type = 0;
	    }

	    WorkerDAO wdao = DataAccessHelper.getDataAccessHelper().getWorkerDAO();
	    wdao.addPaychack(-1, model.getId(), type, amound, name, LocalDate.now());
	    workersController.calculate();
	    workersController.refreshPayements();
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
	Stage stage = (Stage) workRadio.getScene().getWindow();
	stage.close();
    }
}
