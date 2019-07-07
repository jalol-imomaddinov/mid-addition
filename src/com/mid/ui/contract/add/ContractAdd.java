package com.mid.ui.contract.add;

import com.jfoenix.controls.*;
import com.mid.data.common.*;
import com.mid.data.database.*;
import com.mid.data.model.*;
import com.mid.ui.alert.AlertMaker;
import com.mid.ui.contract.add.calc.*;
import com.mid.ui.control.*;
import com.mid.ui.control.converter.*;
import com.mid.ui.error.ErrorLog;
import com.mid.ui.main.MainController;
import com.mid.util.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ContractAdd implements Initializable {

    @FXML
    private StackPane rootPane;
    @FXML
    private HBox mainContainer;
    /**
     * PRODUCT DATA
     */
    @FXML
    private Label orderNumberLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private JFXTextField catalogNumberField;
    @FXML
    private JFXTextField widthField;
    @FXML
    private JFXTextField heightField;

    /**
     * CUSTOMER DATA
     */
    @FXML
    private JFXTextField customerNameField;
    @FXML
    private JFXTextField customerPhoneField;

    /**
     * EXTRA MATERIALS DATA
     */
    @FXML
    private JFXComboBox<WorkerModel> bindedCombo;

    /**
     * AMOUNDS DATA
     */
    @FXML
    private JFXTextField sourceAmoundField;
    @FXML
    private JFXTextField agreedAmoundField;
    @FXML
    private JFXTextField prepaidAmoundField;
    @FXML
    private JFXTextField remaindAmoundField;

    /**
     * DATES DATA
     */
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXDatePicker completeDate;

    @FXML
    private AnchorPane containPane;
    @FXML
    private JFXTextArea noteArea;
    @FXML
    private StackPane sidebar;

    /**
     * IMAGES DATA
     */
    @FXML
    private ImageView productImageView;
    @FXML
    private JFXButton saveButton;

    private FullContractModel fullModel;

    private ProductModel productModel;

    private ProductDAO catalogDAO;
    private ContractDAO contractDAO;
    private WorkerDAO workerDAO;

    private ImageViewerController imageViewer;

    private CalcStateControl calcState;
    private Calculator calculator;

    private int nextContractNumber;

    private boolean save;

    private MainController mainController;

    private PriceBar priceBar;

    private boolean blockCalculate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	imageViewer = ImageViewerController.getImageViewerController();

	catalogDAO = DataAccessHelper.getDataAccessHelper().getCatalogDAO();
	contractDAO = DataAccessHelper.getDataAccessHelper().getContractDAO();
        workerDAO = DataAccessHelper.getDataAccessHelper().getWorkerDAO();

	fullModel = new FullContractModel();
	calcState = new CalcStateControl();

	calculator = new Calculator();

	calcState.setCallback((Boolean t) -> {
	    if (t) {
		calculate();
	    }
	    saveButton.setDisable(!t);
	});

	initializeFields();
	initializeCombos();
	clearCombos();
	initPriceBar();
        initBindedWorkers();
	try {
            workerDAO.getWorkers().addListener((ListChangeListener.Change<? extends WorkerModel> c) -> {
                initBindedWorkers();
            });
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    public void setContractOverview(MainController contractOverview) {
	this.mainController = contractOverview;
    }

    private void initBindedWorkers() {
        try {
            ObservableList<WorkerModel> workers = workerDAO.getWorkers();
            bindedCombo.getItems().clear();
            bindedCombo.getItems().addAll(workers);
        } catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
        }
    }
    
    private void initPriceBar() {
	WindowContainer container = MIDUtil.loadFXML(getClass().getResource("price-bar.fxml"));
	priceBar = container.getController();
	sidebar.getChildren().add(container.getParent());

	priceBar.setCalculator(calculator);
	priceBar.setContractAdd(this);
    }

    private void initializeFields() {

	catalogNumberField.setTextFormatter(new TextFormatter<>(new IntegerConverter((Boolean t) -> {
	    if (t) {
		try {
		    initProduct(catalogNumberField.getText());
		}
		catch (SQLException ex) {
		    ErrorLog.getErrorLog().show(ex);
		}
		calcState.setCatalogFlag(t);
	    }
	})));

	widthField.setTextFormatter(new TextFormatter<>(new DoubleConverter(calcState::setWidthFlag)));
	heightField.setTextFormatter(new TextFormatter<>(new DoubleConverter(calcState::setHeightFlag)));

	sourceAmoundField.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));

	agreedAmoundField.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));
	remaindAmoundField.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));

	prepaidAmoundField.setTextFormatter(new TextFormatter<>(new AmoundConverter((Boolean t) -> {
	    if (t) {
		double agreed = StringUtil.fromAmound(agreedAmoundField.getText());
		double prepaid = StringUtil.fromAmound(prepaidAmoundField.getText());
		remaindAmoundField.setText(StringUtil.toAmound(agreed - prepaid));
	    }
	})));
    }

    private void initializeCombos() {
    }

    @FXML
    private void showProductImage(MouseEvent event) {
	Image image = productImageView.getImage();
	if (image != null) {
	    imageViewer.setImage(image);
	    imageViewer.show();
	}
    }

    @FXML
    private void saveAction(ActionEvent event) {

	try {

	    CalcResult result = calculator.getResult();

	    fullModel.setCatalog(ControlUtil.getFieldString(catalogNumberField));
            
            WorkerModel bindedWorker = ControlUtil.getComboItem(bindedCombo);
            fullModel.setBindedWoker(bindedWorker.getId());

	    fullModel.setProductType(productModel.getProductType());

	    fullModel.setWidth(ControlUtil.getFieldDouble(widthField));
	    fullModel.setHeight(ControlUtil.getFieldDouble(heightField));

	    fullModel.setLimitStart(ControlUtil.getFieldDate(startDate));
	    fullModel.setLimitEnd(ControlUtil.getFieldDate(completeDate));

	    fullModel.setNote(noteArea.getText().trim());
	    fullModel.setNumber(nextContractNumber);

	    fullModel.setOwnerName(ControlUtil.getFieldString(customerNameField));
	    fullModel.setOwnerNumber(ControlUtil.getFieldString(customerPhoneField));

	    fullModel.setWelderPay(result.getWelder());
	    fullModel.setPainterPay(result.getPainter());

	    fullModel.setWelderSqrPay(result.getWelderSqrPrice());
	    fullModel.setPainterSqrPay(result.getPainterSqrPrice());

	    fullModel.setSourceAmound(ControlUtil.getFieldAmound(sourceAmoundField));
	    fullModel.setAgreedAmound(ControlUtil.getFieldAmound(agreedAmoundField));
	    fullModel.setPrepaidAmound(ControlUtil.getFieldAmound(prepaidAmoundField));
	    fullModel.setRemaindAmound(ControlUtil.getFieldAmound(remaindAmoundField));

	    try {
		if (save) {
		    contractDAO.saveContract(fullModel);
		}
		else {
		    contractDAO.updateContract(fullModel);
		}

		if (mainController != null) {
		    mainController.updateData();
		}

	    }
	    catch (SQLException ex) {
		ErrorLog.getErrorLog().show(ex);
	    }

	    getStage().close();

	}
	catch (IllegalFieldValueException ex) {
	    AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Хато маълумот", ex.getMessage());
	}
    }

    @FXML
    private void cancelAction(ActionEvent actionEvent) {
	getStage().close();
    }

    public void calculate() {
	if (blockCalculate) {
	    return;
	}
	if (!calcState.isSuccess()) {
	    return;
	}

	calculator.setProduct(productModel);

	double w = StringUtil.fromAmound(widthField.getText());
	double h = StringUtil.fromAmound(heightField.getText());

	calculator.setSize(w, h);

	calculator.calculate();
	priceBar.updateData();
        
	sourceAmoundField.setText(Double.toString(calculator.getResult().getAmount()));
    }

    public void newModel(int nextNumber) {

	blockCalculate = true;

	clearEntries();

	this.nextContractNumber = nextNumber;
	this.orderNumberLabel.setText(Integer.toString(nextNumber));
	this.save = true;
	this.calculator.getManualData().clear();
	this.priceBar.clear();

	blockCalculate = false;
    }
    public void loadModel(SimpleContractModel simpleModel) {
	try {
	    fullModel = contractDAO.readFullContract(simpleModel.getId(), fullModel);
	    loadModel(fullModel);
	}
	catch (SQLException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public void loadModel(FullContractModel model) {

	try {

	    blockCalculate = true;

	    clearFields();
	    clearCombos();

	    calcState.clear();

	    productModel = null;
	    /**
	     * PRODUCT DATA LOAD
	     */
	    
	    fullModel = model;
	    
	    if (!initProduct(fullModel.getCatalog())) {
		ErrorLog.getErrorLog().show("Catalog " + model.getCatalog() + " not found");
	    }

//	    setTitle(fullModel.getNumber(), fullModel.getProductType(), fullModel.getProductClass(), fullModel.getLatticeType());

	    nextContractNumber = fullModel.getNumber();
	    orderNumberLabel.setText(Integer.toString(nextContractNumber));

	    catalogNumberField.setText(fullModel.getCatalog());

	    widthField.setText(Double.toString(fullModel.getWidth()));
	    heightField.setText(Double.toString(fullModel.getHeight()));

	    /**
	     * CUSTOMER DATA LOAD
	     */
	    noteArea.setText(fullModel.getNote());

	    customerNameField.setText(fullModel.getOwnerName());
	    customerPhoneField.setText(fullModel.getOwnerNumber());

	    /**
	     * CORNICE DATA LOAD
	     */

	    /**
	     * EXTRA MATERIALS LOAD
	     */
          if (fullModel.getBindedWoker() == -1)
                bindedCombo.getSelectionModel().clearSelection();
            else
                bindedCombo.getSelectionModel().select(workerDAO.getWorkerById(fullModel.getBindedWoker()));

	    /**
	     * AMOUNDS LOAD
	     */
	    sourceAmoundField.setText(Double.toString(fullModel.getSourceAmound()));
	    agreedAmoundField.setText(Double.toString(fullModel.getAgreedAmound()));
	    prepaidAmoundField.setText(Double.toString(fullModel.getPrepaidAmound()));
	    remaindAmoundField.setText(Double.toString(fullModel.getRemaindAmound()));

	    /**
	     * DATE LOAD
	     */
	    startDate.setValue(fullModel.getLimitStart());
	    completeDate.setValue(fullModel.getLimitEnd());

	    /**
	     * PUBLIC_DATA LOAD
	     */
//	    System.out.println("{LOAD-PRICES}");
	    CalcResult result = calculator.getResult();
	    result.blockResult();
	    result.clear();

	    result.setAmount(fullModel.getSourceAmound());
	    result.setPainter(fullModel.getPainterPay());
	    result.setPainterSqrPrice(fullModel.getPainterSqrPay());
	    result.setWelder(fullModel.getWelderPay());
	    result.setWelderSqrPrice(fullModel.getWelderSqrPay());

	    priceBar.setManual(true);
	    priceBar.clear();
	    priceBar.refreshEntries();
	    priceBar.applyChanges();

	    result.unblockResult();
	    save = false;
	    blockCalculate = false;
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    private boolean initProduct(String catalog) throws SQLException {
	productModel = catalogDAO.getProductByName(catalog);
        
	if (productModel == null) {
	    return false;
	}
        
        if (productModel.getProductType() == ProductType.RAILING) {
            widthField.setPromptText("Погонный метр");
            heightField.setPromptText("");
            heightField.setDisable(true);
        }
        switch(productModel.getProductType()) {
            case RAILING: {
                widthField.setPromptText("Погонный метр");
                heightField.setPromptText("");
                heightField.setDisable(true);
                break;
            }
            case BENCH: {
                widthField.setPromptText("Эни");
                heightField.setPromptText("");
                heightField.setDisable(true);
            }
            case SWING: {
                widthField.setPromptText("Эни");
                heightField.setPromptText("Буйи");
                heightField.setDisable(false);
            }
            case CAPRICORN: {
                widthField.setPromptText("Эни");
                heightField.setPromptText("Девордан чикиши");
                heightField.setDisable(false);
            }
            default: {
                throw new RuntimeException("INIT PRoduct method: Other product type!");
            }
        }
        
	Image image = catalogDAO.loadProductImage(catalog);
	productImageView.setImage(image);
        nameLabel.setText(productModel.getTypeStr());
	return true;
    }

    private void setTitle(int number, ProductType productType) {

	StringBuilder builder = new StringBuilder(productType.getName());
	nameLabel.setText(builder.toString());
	orderNumberLabel.setText(Integer.toString(number));
    }

    private void clearEntries() {
	clearCombos();
	clearLabels();
	clearFields();
	clearDates();
	productImageView.setImage(null);
    }

    private void clearCombos() {
    }

    private void clearLabels() {
	nameLabel.setText("");
	orderNumberLabel.setText("");
    }

    private void clearFields() {
	noteArea.clear();
	clearField(catalogNumberField);
	clearField(widthField);
	clearField(heightField);
	clearField(customerNameField);
	clearField(customerPhoneField);
	clearField(sourceAmoundField);
	clearField(agreedAmoundField);
	clearField(prepaidAmoundField);
	clearField(remaindAmoundField);
    }

    private void clearField(JFXTextField textField) {
	textField.clear();
    }

    private void clearDates() {
	startDate.getEditor().clear();
	startDate.setValue(LocalDate.now());
	completeDate.getEditor().clear();
	completeDate.setValue(null);
    }

    private Stage getStage() {
	return (Stage) agreedAmoundField.getScene().getWindow();
    }
}
