package com.mid.ui.settings;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.mid.data.common.Theme;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.DatabaseHandler;
import com.mid.data.database.DynamicDAO;
import com.mid.data.database.ExportImportManager;
import com.mid.data.database.Paths;
import com.mid.ui.error.ErrorLog;
import com.mid.util.MIDUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

    @FXML
    private JFXComboBox<Theme> themeCombo;
    @FXML
    private JFXTabPane blurPane;
    @FXML
    private StackPane waitIndicator;
    @FXML
    private JFXTextField mobileNumber;
    @FXML
    private JFXTextField homeNumber;

    private final GaussianBlur gaussianBlur = new GaussianBlur(8);

    private FileChooser chooser = new FileChooser();
    private final FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Резерв файл", "*.mbp");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	themeCombo.getItems().addAll(Theme.values());

	try {
	    DynamicDAO dynamicDAO = DataAccessHelper.getDataAccessHelper().getDynamicDAO();
	    String themeName = dynamicDAO.getTheme();
	    Theme theme = Theme.getTheme(themeName);
	    themeCombo.getSelectionModel().select(theme);
	    
	    String mobile = dynamicDAO.getMobile();
	    String home = dynamicDAO.getHome();
	    
	    mobileNumber.setText(mobile);
	    homeNumber.setText(home);
	}
	catch (SQLException ex) {
	    themeCombo.getSelectionModel().select(0);
	    mobileNumber.setText("+998 123 45 67");
	    homeNumber.setText("123 45 67");
	}
    }

    @FXML
    private void onImportAction(ActionEvent event) {
	chooser.getExtensionFilters().setAll(filter);

	File file = openFile();
	if (file == null) {
	    return;
	}

	Thread thread = new Thread(() -> {
	    try {
		showWaitIndicator();
		DatabaseHandler.closeConnections();
                File backupFile = new File(Paths.BACKUP_PATH + "autobackup_" + LocalDate.now().toString()+ ".mbp");
                ExportImportManager.doExport(backupFile);
                ExportImportManager.doImport(file);
		hideWaitIndicator();
	    }
	    catch (SQLException | IOException ex) {
                Platform.runLater(()-> {
                    ErrorLog.getErrorLog().show(ex);
                });
	    }
	});
	thread.start();
    }

    @FXML
    private void onExportAction(ActionEvent event) {
	chooser.getExtensionFilters().setAll(filter);
	chooser.setInitialFileName("Backup_" + LocalDate.now().toString());

	File file = saveFile();
	if (file == null) {
	    return;
	}

	Thread thread = new Thread(() -> {
	    try {
		showWaitIndicator();
                ExportImportManager.doExport(file);
		hideWaitIndicator();
	    }
	    catch (IOException ex) {
                Platform.runLater(()-> {
                    ErrorLog.getErrorLog().show(ex);
                });
	    }
	});

	thread.start();
    }

    @FXML
    private void onSaveAction(ActionEvent event) {
	try {
	    Theme theme = themeCombo.getValue();

	    String mobile = mobileNumber.getText().trim();
	    String home = homeNumber.getText().trim();
	    
	    DynamicDAO dynamicDAO = DataAccessHelper.getDataAccessHelper().getDynamicDAO();
	    dynamicDAO.setTheme(theme.getIdentifier());
	    dynamicDAO.setMobile(mobile);
	    dynamicDAO.setHome(home);
	    
	    MIDUtil.updateStylesheet(theme.getLocation());
	    closeStage();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void onCancelAction(ActionEvent event) {
	closeStage();
    }

    @FXML
    private void onUserDataClearAction(ActionEvent event) {
    }

    private void hideWaitIndicator() {
	waitIndicator.setVisible(false);
	blurPane.setEffect(null);
    }

    private void showWaitIndicator() {
	waitIndicator.setVisible(true);
	blurPane.setEffect(gaussianBlur);
    }

    private File openFile() {
	File file = chooser.showOpenDialog(getStage());
	return file;
    }

    private File saveFile() {

	File file = chooser.showSaveDialog(getStage());
	if (file == null) {
	    return null;
	}
	if (file.exists()) {
	    file.delete();
	}
	try {
	    file.createNewFile();
	}
	catch (IOException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}

	return file;
    }

    private void closeStage() {
	((Stage) themeCombo.getScene().getWindow()).close();
    }

    private Stage getStage() {
	return ((Stage) themeCombo.getScene().getWindow());
    }
}