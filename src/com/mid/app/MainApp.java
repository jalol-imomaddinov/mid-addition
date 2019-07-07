package com.mid.app;

import com.mid.data.common.Theme;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.DatabaseHandler;
import com.mid.data.database.ExportImportManager;
import com.mid.data.database.Paths;
import com.mid.data.database.SerialChecker;
import com.mid.ui.error.ErrorLog;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author
 */
public class MainApp extends Application {

    private static Stage primaryStage;
    private static MainApp mainApp;

    public static Stage getPrimaryStage() {
	return primaryStage;
    }

    public static MainApp getMainApp() {
	return mainApp;
    }

    public static void main(String[] args) {
	launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

	mainApp = this;
	primaryStage = stage;

	if (!SerialChecker.checkSerial()) {
	    WindowContainer container = MIDUtil.loadFXML(getClass().getResource("/com/mid/ui/serial/serial.fxml"));

	    Parent root = container.getParent();
            root.getStylesheets().add(Theme.BLUE.getLocation());
	    Scene scene = new Scene(root);

	    MIDUtil.setStageIcon(primaryStage);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    primaryStage.setTitle("MID - Check serial");
	}
	else {
	    loadMainApp();
	}
    }

    public void loadMainApp() {

	initDatas();
	WindowContainer container = MIDUtil.loadFXML(getClass().getResource("/com/mid/ui/main/main.fxml"));

	Parent root = container.getParent();

	Scene scene = new Scene(root);

	MIDUtil.setStageIcon(primaryStage);
	primaryStage.setScene(scene);
	primaryStage.setTitle("MID::Addition");
	primaryStage.setMaximized(true);
	primaryStage.show();

    }

    private void initDatas() {
	try {
	    checkFirstRun();

	    DataAccessHelper accessHelper = DataAccessHelper.getDataAccessHelper();
	    String identifier = accessHelper.getDynamicDAO().getTheme();
	    Theme theme = Theme.getTheme(identifier);
	    MIDUtil.setStylesheet(theme.getLocation());
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    private void checkFirstRun() {

	File firstRun = new File(Paths.FIRST_RUN_FILE);

	if (!firstRun.exists()) {
	    try {
		Paths.createPaths();
                ExportImportManager.doImport(new File(Paths.EMPTY_BASE));
		firstRun.createNewFile();
	    }
	    catch (IOException ex) {
		throw new RuntimeException(ex);
	    }
	}
    }

    private Rectangle2D screenBounds() {
	return Screen.getPrimary().getVisualBounds();
    }
}
