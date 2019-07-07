package com.mid.ui.contract.add;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.mid.ui.contract.add.calc.CalcResult;
import com.mid.ui.contract.add.calc.CalculateData;
import com.mid.ui.contract.add.calc.Calculator;
import com.mid.ui.control.ControlUtil;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.ui.control.converter.AmoundConverter;
import com.mid.util.StringUtil;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;

public class PriceBar implements Initializable {

    @FXML
    private JFXToggleButton manualToggle;
    @FXML
    private Label productSquareLabel;
    @FXML
    private Label welderPayLabel;
    @FXML
    private Label painterPayLabel;

    @FXML
    private JFXTextField squarePriceField;
    @FXML
    private JFXTextField welderSquarePriceField;
    @FXML
    private JFXTextField painterSquarePriceField;

    private Calculator calculator;

    private ContractAdd contractAdd;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	Consumer<Boolean> changeApplier = (Boolean t) -> {
	    applyChanges();
	};

	squarePriceField.setTextFormatter(new TextFormatter<>(new AmoundConverter(changeApplier)));
	welderSquarePriceField.setTextFormatter(new TextFormatter<>(new AmoundConverter(changeApplier)));
	painterSquarePriceField.setTextFormatter(new TextFormatter<>(new AmoundConverter(changeApplier)));

	manualToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    calculator.getManualData().setEnable(newValue);
	    if (newValue) {
		updateData();
	    }
	});
    }

    @FXML
    private void refreshAction(ActionEvent event) {
	if (!isManual()) {
	    contractAdd.calculate();
	}
    }

    public void applyChanges() {
	try {

	    if (!isManual()) {
		return;
	    }

	    double price = ControlUtil.getFieldAmound(squarePriceField);
	    double w = ControlUtil.getFieldAmound(welderSquarePriceField);
	    double p = ControlUtil.getFieldAmound(painterSquarePriceField);

	    CalculateData manualData = calculator.getManualData();
	    manualData.setPrice(price);
	    manualData.setWelderPay(w);
	    manualData.setPainterPay(p);

	    contractAdd.calculate();

	}
	catch (IllegalFieldValueException ex) {
	}
    }

    public void updateData() {

	CalcResult result = calculator.getResult();

	String price = StringUtil.toAmound(result.getPrice());

//	welderPayLabel.setText(StringUtil.toAmound(result.getWelder()));
//	painterPayLabel.setText(StringUtil.toAmound(result.getPainter()));

	if (!isManual()) {
	    refreshEntries();
	}
    }

    public void refreshEntries() {

	CalcResult result = calculator.getResult();
        squarePriceField.setText(Double.toString(result.getPrice()));
	welderSquarePriceField.setText(Double.toString(result.getWelderSqrPrice()));
	painterSquarePriceField.setText(Double.toString(result.getPainterSqrPrice()));
    }

    public boolean isManual() {
	return manualToggle.isSelected();
    }

    public void setManual(boolean manual) {
	manualToggle.setSelected(manual);
    }

    public void setContractAdd(ContractAdd contractAdd) {
	this.contractAdd = contractAdd;
    }

    public void setCalculator(Calculator calculator) {
	this.calculator = calculator;
    }

    public void clear() {
	squarePriceField.setText(Double.toString(0));
	welderSquarePriceField.setText(Double.toString(0));
	painterSquarePriceField.setText(Double.toString(0));

	productSquareLabel.setText("-");
//	painterPayLabel.setText("-");
//	welderPayLabel.setText("-");
    }
}
