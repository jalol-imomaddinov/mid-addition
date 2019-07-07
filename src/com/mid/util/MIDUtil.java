package com.mid.util;

import com.mid.ui.error.ErrorLog;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MIDUtil {

    public static final String ICON_IMAGE_LOC = "/resources/icon2.png";

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private static final HashMap<URL, WindowContainer> windowCache = new HashMap<>();
    private static final ArrayList<WindowContainer> viewCache = new ArrayList<>();

    public static String STYLESHEET = "";
    public static String LAST_STYLESHEET = "";

    public static void setStageIcon(Stage stage) {
	stage.getIcons().add(new Image(ICON_IMAGE_LOC));
    }

    public static WindowContainer loadFXML(URL loc, boolean b) {

	WindowContainer container = null;
	Object controller;

	try {
	    container = windowCache.get(loc);

	    if (container == null) {

		FXMLLoader loader = new FXMLLoader(loc);
		Parent parent = loader.load();

		if (b) {
		    parent.getStylesheets().add(STYLESHEET);
		}

		controller = loader.getController();

		container = new WindowContainer(parent, controller);

		viewCache.add(container);
	    }
	}
	catch (IOException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}

	return container;
    }

    public static WindowContainer loadFXML(URL loc) {
	return loadFXML(loc, true);
    }

    public static WindowContainer loadWindow(URL loc, String title, boolean isShow, Stage parentStage) {

	WindowContainer container = null;
	Stage stage;
	Object controller;

	try {

	    container = windowCache.get(loc);

	    if (container == null) {

		FXMLLoader loader = new FXMLLoader(loc);

		Parent parent = loader.load();
		parent.getStylesheets().add(STYLESHEET);

		controller = loader.getController();

		if (parentStage != null) {
		    stage = parentStage;
		}
		else {
		    stage = new Stage(StageStyle.DECORATED);
		}

		stage.setTitle(title);
		stage.setScene(new Scene(parent));
		setStageIcon(stage);

		container = new WindowContainer(stage, parent, controller);

		windowCache.put(loc, container);
		viewCache.add(container);
	    }
	    else {
		stage = container.getStage();
	    }

	    stage.toFront();
	    stage.setTitle(title);
	    if (isShow) {
		stage.show();
	    }
	}
	catch (IOException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}

	return container;
    }

    public static void setStylesheet(String stylesheet) {
	STYLESHEET = stylesheet;
    }

    public static void updateStylesheet(String stylesheet) {
	if (STYLESHEET.equals(stylesheet)) {
	    return;
	}

	LAST_STYLESHEET = STYLESHEET;
	STYLESHEET = stylesheet;

	for (WindowContainer view : viewCache) {
	    view.getParent().getStylesheets().remove(LAST_STYLESHEET);
	    view.getParent().getStylesheets().add(STYLESHEET);
	}
    }

    public static String formatDateTimeString(Date date) {
	return DATE_TIME_FORMAT.format(date);
    }

    public static String formatDateTimeString(Long time) {
	return DATE_TIME_FORMAT.format(new Date(time));
    }

    public static String getDateString(Date date) {
	return DATE_FORMAT.format(date);
    }

    public static boolean validateEmailAddress(String emailID) {
	String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	Pattern pattern = Pattern.compile(regex);
	return pattern.matcher(emailID).matches();
    }
}
