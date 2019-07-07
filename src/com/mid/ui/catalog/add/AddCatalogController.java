package com.mid.ui.catalog.add;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mid.data.common.ProductType;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.Paths;
import com.mid.data.database.ProductDAO;
import com.mid.data.model.ProductModel;
import com.mid.ui.catalog.CatalogOverview;
import com.mid.ui.control.ControlUtil;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.ui.control.converter.AmoundConverter;
import com.mid.ui.control.converter.DoubleConverter;
import com.mid.ui.control.converter.TextConverter;
import com.mid.ui.error.ErrorLog;
import com.mid.util.IOUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddCatalogController implements Initializable {

    @FXML
    private JFXTextField numberField;
    @FXML
    private JFXComboBox<ProductType> typeCombo;
    @FXML
    private JFXTextField welderPay;
    @FXML
    private JFXTextField painterPay;
    @FXML
    private JFXTextField amoundField;
    @FXML
    private ImageView imageView;

    private ProductDAO productDAO;

    private ProductModel model;

    private boolean save;

    private FileChooser fileChooser;
    private boolean fromCatalog;
    private File lastPath;
    private File imagepath;

    private CatalogOverview catalogOverview;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	amoundField.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));
	painterPay.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));
	welderPay.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));

	productDAO = DataAccessHelper.getDataAccessHelper().getCatalogDAO();

	numberField.setTextFormatter(new TextFormatter<>(new TextConverter((t) -> {
	    acceptCatalog(t);
	})));

	typeCombo.getItems().addAll(ProductType.values());

	typeCombo.getSelectionModel().select(0);

	fileChooser = new FileChooser();
	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Расмлар", "*.jpg", "*.jpeg", "*.png", "*.gif"));
	fromCatalog = false;
    }

    public void setCatalogOverview(CatalogOverview catalogOverview) {
	this.catalogOverview = catalogOverview;
    }

    public void newModel() {
	this.save = true;
	numberField.clear();
        amoundField.clear();
        painterPay.clear();
        welderPay.clear();
	typeCombo.getSelectionModel().select(0);
	imageView.setImage(null);
    }

    public void loadModel(ProductModel model) {
	this.save = false;
	this.model = model;

	numberField.setText(model.getCatalog());
	typeCombo.getSelectionModel().select(model.getProductType());
        amoundField.setText(model.getPriceStr());
        welderPay.setText(model.getWelderStr());
        painterPay.setText(model.getPainterStr());
	Image image = productDAO.loadProductImage(model.getCatalog());
	imageView.setImage(image);
	fromCatalog = true;
    }

    private void acceptCatalog(Boolean t) {
	try {
            Image image = productDAO.loadProductImage(ControlUtil.getFieldString(numberField));
            if (image != null && imageView.getImage() == null) {
                imageView.setImage(image);
                fromCatalog = image != null;
            }
	}
	catch (IllegalFieldValueException ex) {
	}
    }

    @FXML
    private void setPhoto(MouseEvent event) {

	if (lastPath != null) {
	    fileChooser.setInitialDirectory(lastPath);
	}
	imagepath = fileChooser.showOpenDialog(imageView.getScene().getWindow());

	if (imagepath != null) {

	    lastPath = imagepath.getParentFile();
	    Image img = null;

	    try {
		img = new Image(new FileInputStream(imagepath));
		fromCatalog = false;
	    }
	    catch (FileNotFoundException ex) {
		ErrorLog.getErrorLog().show(ex);
	    }
	    imageView.setImage(img);
	}
    }

    @FXML
    private void saveAction(ActionEvent event) {
	try {
	    String catalog = ControlUtil.getFieldString(numberField);
            double welder = ControlUtil.getFieldAmound(welderPay);
            double painter = ControlUtil.getFieldAmound(painterPay);
            double amound = ControlUtil.getFieldAmound(amoundField);
	    ProductType productType = typeCombo.getValue();

	    if (save) {
		model = new ProductModel();
	    }

	    model.setCatalog(catalog);
	    model.setProductType(productType);
	    model.setPainter(painter);
            model.setWelder(welder);
            model.setPrice(amound);

	    if (!fromCatalog) {
		if (imagepath.exists()) {
		    imagepath.delete();
		}
                
		IOUtil.copy(imagepath.getAbsolutePath(), Paths.IMAGE_PATH + catalog + ".jpg");
	    }

	    if (save) {
		productDAO.saveProduct(model);
	    }
	    else {
		productDAO.updateProduct(model);
	    }

	    catalogOverview.handleReinitProducts(productType);
	    closeStage();
	}
	catch (IllegalFieldValueException ex) {
	}
	catch (SQLException | IOException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void cancelAction(ActionEvent event) {
	closeStage();
    }

    private void closeStage() {
	Stage stage = (Stage) imageView.getScene().getWindow();
	stage.close();
    }
}
