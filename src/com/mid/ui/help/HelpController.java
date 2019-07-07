package com.mid.ui.help;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelpController implements Initializable {

    @FXML
    private ImageView imageView;

    private boolean inited = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	imageView.setImage(new Image(getClass().getResourceAsStream("/resources/splash.jpg")));
	imageView.setOnMouseClicked((MouseEvent event) -> {
	    getStage().close();
	});
    }

    public void initStyles() {
	if (inited) {
	    return;
	}

	getStage().setAlwaysOnTop(true);
	getStage().initStyle(StageStyle.TRANSPARENT);
	imageView.getScene().setFill(Color.TRANSPARENT);
	inited = true;
    }

    private Stage getStage() {
	Stage stage = (Stage) imageView.getScene().getWindow();
	return stage;
    }
}
