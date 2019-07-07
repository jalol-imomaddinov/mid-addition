package com.mid.ui.control;

import com.mid.app.MainApp;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmerController extends Stage implements Initializable {

    private static ConfirmerController confirmer;
    
    public static ConfirmerController getConfirmer() {
	if (confirmer == null) {
	    WindowContainer container = MIDUtil.loadFXML(ConfirmerController.class.getResource("/com/mid/ui/control/confirmer.fxml"));
	    confirmer = container.getController();
	    Scene scene = new Scene(container.getParent(), Color.TRANSPARENT);
	    confirmer.setScene(scene);
	}
	return confirmer;
    }
    
    @FXML
    private Label title;
    @FXML
    private Label Message;
    
    private Consumer<Boolean> callback;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	initStyle(StageStyle.TRANSPARENT);
	initModality(Modality.APPLICATION_MODAL);
	initOwner(MainApp.getPrimaryStage());
	setTitle("Подтверждение");
    }

    public void setTitleText(String title) {
	this.title.setText(title);
    }
    
    public void setMessage(String msg) {
	this.Message.setText(msg);
    }
    
    public void setCallback(Consumer<Boolean> callback) {
	this.callback = callback;
    }
    
    @FXML
    private void yesAction(ActionEvent event) {
	callback.accept(Boolean.TRUE);
    }

    @FXML
    private void noAction(ActionEvent event) {
	callback.accept(Boolean.FALSE);
    }
}