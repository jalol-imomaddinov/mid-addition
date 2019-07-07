package com.mid.ui.serial;

import com.jfoenix.controls.JFXTextField;
import com.mid.app.MainApp;
import com.mid.data.database.SerialChecker;
import com.mid.ui.settings.Preferences;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SerialController implements Initializable {

    @FXML
    private JFXTextField keyCodeField;
    @FXML
    private Button submitButton;
    @FXML
    private WebView readMeWebView;

    private Preferences preferences;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	keyCodeField.textProperty().addListener((observable, oldValue, newValue) -> {
	    submitButton.setDisable((newValue.length() < 7));
	});

	preferences = Preferences.getPreferences();
    }

    @FXML
    private void onSubmitAction(ActionEvent event) {
	String serialCode = keyCodeField.getText().trim();

	boolean validSerial = SerialChecker.validSerial(serialCode);

	if (validSerial) {
	    SerialChecker.saveSerial();
	    loadMain();
	}
	else {
	    keyCodeField.getStyleClass().add("wrong-credentials");
	}
    }

    @FXML
    private void onCloseAction(ActionEvent event) {
	Platform.exit();
    }

    private void closeStage() {
	((Stage) keyCodeField.getScene().getWindow()).close();
    }

    private void loadMain() {

	closeStage();

	MainApp.getMainApp().loadMainApp();
    }
}
