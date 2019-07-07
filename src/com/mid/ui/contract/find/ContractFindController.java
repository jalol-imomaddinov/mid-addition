package com.mid.ui.contract.find;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.mid.ui.control.ControlUtil;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.util.DateUtil;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContractFindController implements Initializable {

    @FXML
    private JFXRadioButton numberRadio;
    @FXML
    private JFXTextField numberField;
    @FXML
    private JFXRadioButton datasRadio;
    @FXML
    private VBox datasVBox;
    @FXML
    private JFXCheckBox catalogCheck;
    @FXML
    private JFXTextField catalogField;
    @FXML
    private JFXCheckBox customerCheck;
    @FXML
    private JFXTextField customerField;
    @FXML
    private JFXCheckBox dateCheck;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXDatePicker endDate;
    @FXML
    private HBox dateHBox;

    private ToggleGroup group;
    
    private Consumer<String> callback;

    private final StringBuilder queryBuilder = new StringBuilder();
    private final StringBuilder labelBuilder = new StringBuilder();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	group = new ToggleGroup();
	group.getToggles().addAll(numberRadio, datasRadio);

	numberRadio.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    numberField.setDisable(!newValue);
	    datasVBox.setDisable(newValue);
	});

	datasRadio.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    numberField.setDisable(newValue);
	    datasVBox.setDisable(!newValue);
	});

	catalogCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    catalogField.setDisable(!newValue);
	});

	customerCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    customerField.setDisable(!newValue);
	});

	dateCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    dateHBox.setDisable(!newValue);
	});

	clearEntries();
    }

    public void setCallback(Consumer<String> callback) {
	this.callback = callback;
    }

    @FXML
    private void findAction(ActionEvent event) {
	
	queryBuilder.setLength(0);
	labelBuilder.setLength(0);
	queryBuilder.append("WHERE ");
	try {
	    if (numberRadio.isSelected()) {
		processNumber();
	    }
	    else {
		boolean bAnd = false;
		if (catalogCheck.isSelected()) {
		    processCatalog();
		    bAnd = true;
		}
		if (customerCheck.isSelected()) {
		    if (bAnd) {
			queryBuilder.append(" AND ");
			labelBuilder.append(" | ");
		    }
		    processCustomer();
		    bAnd = true;
		}
		if (dateCheck.isSelected()) {
		    if (bAnd) {
			queryBuilder.append(" AND ");
			labelBuilder.append(" | ");
		    }
		    processDate();
		}
	    }

	    this.callback.accept(queryBuilder.toString());
	    numberField.getScene().getWindow().hide();
	}
	catch (IllegalFieldValueException ex) {
	}
    }

    @FXML
    private void cancelAction(ActionEvent event) {
	numberField.getScene().getWindow().hide();
    }

    private void clearEntries() {
	group.selectToggle(numberRadio);

	numberField.setText("");
	catalogField.setText("");
	customerField.setText("");
	startDate.setValue(null);
	startDate.getEditor().setText("");
	endDate.setValue(null);
	endDate.getEditor().setText("");

	catalogCheck.setSelected(false);
	customerCheck.setSelected(false);
	dateCheck.setSelected(false);
    }

    private void processNumber() throws IllegalFieldValueException {
	int number = ControlUtil.getFieldInteger(numberField);
	queryBuilder.append("number == ").append(number);
	labelBuilder.append("№ ").append(number);
    }

    private void processCatalog() throws IllegalFieldValueException {
	int catalog = ControlUtil.getFieldInteger(catalogField);
	queryBuilder.append("(catalog == '").append(catalog).append("')");
	labelBuilder.append("Каталог ").append(catalog);
    }

    private void processCustomer() throws IllegalFieldValueException {
	String customer = ControlUtil.getFieldString(customerField);
	queryBuilder.append("(lower(owner_name) LIKE lower('%").append(customer).append("%'))");
	labelBuilder.append(customer);
    }

    private void processDate() throws IllegalFieldValueException {
	LocalDate start = ControlUtil.getFieldDate(startDate);
	LocalDate end = ControlUtil.getFieldDate(endDate);
	
	queryBuilder.append("(limit_start >= '").append(DateUtil.dbFormat(start)).append("' AND ")
	.append("limit_start <= '").append(DateUtil.dbFormat(end)).append("')");

	labelBuilder.append(DateUtil.viewFormat(start)).append("..").append(DateUtil.viewFormat(end));
    }

    public void setCustomer(String text) {
	String str = text.trim();
	if (!str.equals("")) {
	    datasRadio.setSelected(true);
	    customerCheck.setSelected(true);
	    customerField.setText(str);
	}
    }
}