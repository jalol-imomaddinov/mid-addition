package com.mid.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mid.ui.settings.Preferences;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController implements Initializable {

    private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    Preferences preference;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	preference = Preferences.getPreferences();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
	String uname = username.getText();
	String pword = DigestUtils.shaHex(password.getText());

	/*        if (uname.equals(preference.getUsername()) && pword.equals(preference.getPassword())) {
	 closeStage();
	 loadMain();
	 LOGGER.log(Level.INFO, "User successfully logged in {}", uname);
	 }
	 else {
	 username.getStyleClass().add("wrong-credentials");
	 password.getStyleClass().add("wrong-credentials");
	 }
	 */    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
	System.exit(0);
    }

    private void closeStage() {
	((Stage) username.getScene().getWindow()).close();
    }

    void loadMain() {
	URL url = getClass().getResource("/library/assistant/ui/main/main.fxml");
	WindowContainer container = MIDUtil.loadWindow(url, "MID", true, null);
    }

}
