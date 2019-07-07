package com.mid.ui.error;

import com.jfoenix.controls.JFXTextArea;
import com.mid.data.database.Paths;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class ErrorLog implements Initializable {

    private static ErrorLog errorLog;

    public static ErrorLog getErrorLog() {
	if (errorLog == null) {
	    WindowContainer container = MIDUtil.loadWindow(ErrorLog.class.getResource("error-log.fxml"), "System Error", false, null);
	    container.getStage().setAlwaysOnTop(true);
	    errorLog = container.getController();
	}

	return errorLog;
    }
    @FXML
    private JFXTextArea messageField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void show(Exception e) {
	StringBuilder builder = new StringBuilder();
	builder.append(e.toString()).append(System.lineSeparator());
	for (StackTraceElement stackTrace : e.getStackTrace()) {
	    builder.append(stackTrace.toString()).append(System.lineSeparator());
	}
	show(builder.toString());
	throw new RuntimeException(e);
    }

    public void show(String message) {
	messageField.setText(message);
        saveLog(message);
	getStage().show();
    }

    @FXML
    private void confirmAction(ActionEvent event) {
	getStage().close();
    }

    private Stage getStage() {
	return (Stage) messageField.getScene().getWindow();
    }
    
    private void saveLog(String msg)  {
        try {
            File log = new File(Paths.LOG_PATH, "log_" + LocalDateTime.now().toString());
            log.createNewFile();
            FileOutputStream fos = new FileOutputStream(log);
            fos.write(msg.getBytes());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
