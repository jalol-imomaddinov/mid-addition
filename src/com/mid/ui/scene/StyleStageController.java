package com.mid.ui.scene;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StyleStageController implements Initializable {

    @FXML
    private ImageView stageIcon;
    @FXML
    private Label title;
    @FXML
    private AnchorPane titleBar;

    private Stage stage;

    private double initX;
    private double initY;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
	    public void handle(MouseEvent me) {
		initX = me.getScreenX() - stage.getX();
		initY = me.getScreenY() - stage.getY();
	    }
	});

	//when screen is dragged, translate it accordingly
	titleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
	    public void handle(MouseEvent me) {
		stage.setX(me.getScreenX() - initX);
		stage.setY(me.getScreenY() - initY);
	    }
	});
    }

    public void setTitle(String title) {
	this.title.setText(title);
    }

    public void setIcon(Image i) {
	this.stageIcon.setImage(i);
    }

    @FXML
    private void minimize(ActionEvent event) {
	stage.setIconified(true);
    }

    @FXML
    private void close(ActionEvent event) {
	stage.close();
    }

    public void setStage(Stage stage) {
	this.stage = stage;
    }
}
