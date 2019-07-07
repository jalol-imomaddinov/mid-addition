package com.mid.ui.contract.print;

import com.jfoenix.controls.JFXComboBox;
import com.mid.data.database.ContractDAO;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.model.FullContractModel;
import com.mid.data.model.SimpleContractModel;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ContractPrintController implements Initializable {

    @FXML
    private ImageView previewImageViewer;
    @FXML
    private JFXComboBox<Printer> printerCombo;

    private PreviewController preview;
    @FXML
    private HBox blurBox;
    @FXML
    private ProgressIndicator waitBar;

    private GaussianBlur gaussianBlur = new GaussianBlur(8);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	WindowContainer loader = MIDUtil.loadFXML(getClass().getResource("contract-preview.fxml"), false);

	Parent pane = loader.getParent();
	pane.getStylesheets().add(getClass().getResource("preview.css").toString());
	preview = loader.getController();
	Stage stage = new Stage();
	stage.setScene(new Scene(pane));

	previewImageViewer.setImage(preview.getPreview());
    }

    public void setModel(SimpleContractModel model) throws SQLException {

	hideEffects();

	FullContractModel full = new FullContractModel();

	ContractDAO contractDAO = DataAccessHelper.getDataAccessHelper().getContractDAO();
	contractDAO.readFullContract(model.getId(), full);

	preview.loadModel(full);

	updatePrinters();
    }

    public void updatePrinters() {
	printerCombo.getItems().clear();

	for (Printer printer : Printer.getAllPrinters()) {
	    printerCombo.getItems().add(printer);
	}

	printerCombo.getSelectionModel().select(Printer.getDefaultPrinter());
    }

    @FXML
    private void printAction(ActionEvent event) {

	blurBox.setEffect(gaussianBlur);
	waitBar.setVisible(true);

	Printer printer = printerCombo.getSelectionModel().getSelectedItem();

	PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 0, 0, 0, 0);

	PrinterJob job = PrinterJob.createPrinterJob(printer);

	boolean success = job.printPage(layout, preview.getPrintable());

	hideEffects();

	if (success) {
	    job.endJob();
	    Stage stage = (Stage) printerCombo.getScene().getWindow();
	    stage.close();
	}
    }

    private void hideEffects() {
	blurBox.setEffect(null);
	waitBar.setVisible(false);
    }
}
