package com.mid.ui.control;

import com.mid.util.StringUtil;
import java.time.LocalDate;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * @author
 */
public class ControlUtil {

    public static String getFieldString(TextField textField) throws IllegalFieldValueException {
	String text = textField.getText().trim();

	if (text.isEmpty()) {
	    textField.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + textField.getPromptText() + "\" тулдирилмаган");
	}

	textField.getStyleClass().remove("wrong-credentials");

	return text;
    }

    public static LocalDate getFieldDate(DatePicker picker) throws IllegalFieldValueException {

	LocalDate value = picker.getValue();

	if (value == null) {
	    picker.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + picker.getPromptText() + "\" тулдирилмаган");
	}

	picker.getStyleClass().remove("wrong-credentials");
	return value;
    }

    public static double getFieldDouble(TextField textField) throws IllegalFieldValueException {
	String text = textField.getText().trim();

	if (text.isEmpty()) {
	    textField.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + textField.getPromptText() + "\" тулдирилмаган");
	}

	double v;
	try {
	    v = Double.parseDouble(text);
	    textField.getStyleClass().remove("wrong-credentials");
	}
	catch (NumberFormatException e) {
	    textField.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + textField.getPromptText() + "\" хато тулдирилган");
	}
	return v;
    }

    public static int getFieldInteger(TextField textField) throws IllegalFieldValueException {
	String text = textField.getText().trim();

	if (text.isEmpty()) {
	    textField.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + textField.getPromptText() + "\" тулдирилмаган");
	}

	int v;
	try {
	    v = Integer.parseInt(text);
	    textField.getStyleClass().remove("wrong-credentials");
	}
	catch (NumberFormatException e) {
	    textField.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + textField.getPromptText() + "\" хато тулдирилган");
	}
	return v;
    }
    
    public static double getFieldAmound(TextField textField) throws IllegalFieldValueException {
	String text = textField.getText().trim();

	if (text.isEmpty()) {
	    textField.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + textField.getPromptText() + "\" тулдирилмаган");
	}

	double v;
	try {
	    v = StringUtil.fromAmound(text);
	    textField.getStyleClass().remove("wrong-credentials");
	}
	catch (NumberFormatException e) {
	    textField.getStyleClass().add("wrong-credentials");
	    throw new IllegalFieldValueException("\"" + textField.getPromptText() + "\" хато тулдирилган");
	}
	return v;
    }

    public static <T> T getComboItem(ComboBox comboBox) throws IllegalFieldValueException {
        Object v = comboBox.getSelectionModel().getSelectedItem();
        if (v == null) {
	    throw new IllegalFieldValueException("\"" + comboBox.getPromptText() + "\" сайланмаган");
        }
        return (T) v;
    }
}
