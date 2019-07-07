package com.mid.ui.control;

import com.jfoenix.controls.JFXComboBox;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ImageViewerController implements Initializable {

    public static ImageViewerController getImageViewerController() {

	WindowContainer loader = MIDUtil.loadFXML(ImageViewerController.class.getResource("image-viewer.fxml"));

	Parent pane = loader.getParent();

	ImageViewerController control = loader.getController();

	Stage stage = new Stage(StageStyle.TRANSPARENT);

	Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

	stage.setWidth(bounds.getWidth());
	stage.setHeight(bounds.getHeight());

	stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
	stage.setFullScreenExitHint("");

	stage.setScene(new Scene(pane, Color.TRANSPARENT));
	stage.toFront();

	control.stage = stage;

	return control;
    }

    @FXML
    private ScrollPane container;
    @FXML
    private ImageView imageView;

    private double persentage = 1;
    @FXML
    private Label zoomLabel;

    private Stage stage;

    private PrintCanvasController printCanvas;
    @FXML
    private JFXComboBox<Printer> printer;
    @FXML
    private AnchorPane blurBox;
    @FXML
    private ProgressIndicator waitBar;

    private GaussianBlur gaussianBlur = new GaussianBlur(8);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
	    persentage = 1;
	    changeSizes();
	});

	imageView.setImage(new Image("file:D:/app.jpg"));

	container.widthProperty().addListener((observable, oldValue, newValue) -> {
	    changeSizes();
	});

	container.heightProperty().addListener((observable, oldValue, newValue) -> {
	    changeSizes();
	});

	WindowContainer loadFXML = MIDUtil.loadFXML(getClass().getResource("print-canvas.fxml"));
	printCanvas = (PrintCanvasController) loadFXML.getController();
    }

    public void setImage(Image image) {
	this.imageView.setImage(image);
    }

    public void show() {
	this.printer.getItems().clear();
	this.printer.getItems().addAll(Printer.getAllPrinters());
	this.printer.getSelectionModel().select(Printer.getDefaultPrinter());
	hideEffects();
	this.stage.show();
    }

    @FXML
    private void onScrollFinished(ScrollEvent event) {
    }

    @FXML
    private void onScrollStarted(ScrollEvent event) {
    }

    @FXML
    private void onScroll(ScrollEvent event) {
    }

    @FXML
    private void onClose(ActionEvent event) {
	stage.close();
    }

    private double getWidth() {
	return container.getWidth();
    }

    private double getHeight() {
	return container.getHeight();
    }

    private void changeSizes() {
	zoomLabel.setText(Integer.toString((int) (persentage * 100)) + "%");
	imageView.setFitWidth(getWidth() * persentage);
	imageView.setFitHeight(getHeight() * persentage);
	container.setVvalue(0.5);
	container.setHvalue(0.5);
    }

    @FXML
    private void printAction(ActionEvent event) {

	showEffects();

	printCanvas.setImage(imageView.getImage());
	AnchorPane printable = printCanvas.getPrintable();

	Printer p = printer.getSelectionModel().getSelectedItem();

	PageLayout layout = p.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 0, 0, 0, 0);

	PrinterJob job = PrinterJob.createPrinterJob(p);

	boolean success = job.printPage(layout, printable);
	job.endJob();

	hideEffects();
    }

    private void hideEffects() {
	blurBox.setEffect(null);
	waitBar.setVisible(false);
    }

    private void showEffects() {
	blurBox.setEffect(gaussianBlur);
	waitBar.setVisible(true);
    }

    @FXML
    private void zoomPlusAction(ActionEvent event) {
	persentage += 0.2;
	changeSizes();
    }

    @FXML
    private void zoomMinusAction(ActionEvent event) {
	persentage -= 0.2;
	if (persentage <= 0.1) {
	    persentage = 0.1;
	}
	changeSizes();
    }
}
