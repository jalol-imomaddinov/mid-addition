package com.mid.ui.scene;

import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author
 */
public class StyleStage extends Stage {

    private StyleStageController stageController;
    private StackPane pane;

    public StyleStage() {
	super(StageStyle.TRANSPARENT);
	Parent titleBar = loadContainer();

	VBox box = new VBox();
	box.setFillWidth(true);

	pane = new StackPane();
	pane.setMaxWidth(Double.MAX_VALUE);
	pane.setMaxHeight(Double.MAX_VALUE);

	box.getChildren().addAll(titleBar, pane);
	setScene(new Scene(box));

	stageController.setStage(this);
    }

    private Parent loadContainer() {
	WindowContainer loadFXML = MIDUtil.loadFXML(getClass().getResource("style-stage.fxml"));
	stageController = (StyleStageController) loadFXML.getController();
	return loadFXML.getParent();
    }

    public void setImage(Image image) {
	getIcons().add(image);
	stageController.setIcon(image);
    }

    public void setContent(Parent parent) {
	pane.getChildren().setAll(parent);
    }

    public void changeTitle(String text) {
	super.setTitle(text);
	stageController.setTitle(text);
    }
}
