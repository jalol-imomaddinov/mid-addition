package com.mid.ui.catalog;

import com.mid.data.common.ProductType;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.ProductDAO;
import com.mid.data.model.ProductModel;
import com.mid.ui.catalog.add.AddCatalogController;
import com.mid.ui.error.ErrorLog;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class CatalogOverview implements Initializable {

    @FXML
    private TableView<ProductModel> catalogTable;
    @FXML
    private TableColumn<ProductModel, String> catalogCol;
    @FXML
    private TableColumn<ProductModel, String> typeCol;
    @FXML
    private TableColumn<ProductModel, String> priceCol;
    @FXML
    private TableColumn<ProductModel, String> welderPayCol;
    @FXML
    private TableColumn<ProductModel, String> painterPayCol;
    @FXML
    private ToggleButton railingToggle;
    @FXML
    private ToggleButton benchToggle;
    @FXML
    private ToggleButton swingToggle;
    @FXML
    private ToggleButton capricornToggle;

    private final ToggleGroup productGroup = new ToggleGroup();

    private int currentPage;


    private final int max = 10;

    private final ObservableList<ProductModel> products = FXCollections.observableArrayList();

    private final List<ProductModel> productsCache = new ArrayList<>();

    private ProductDAO productDAO;

    private final HashMap<Toggle, ProductType> switches = new HashMap<>();

    private ProductType switchType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

	catalogCol.setCellValueFactory(new PropertyValueFactory<>("catalog"));
	typeCol.setCellValueFactory(new PropertyValueFactory<>("typeStr"));
	priceCol.setCellValueFactory(new PropertyValueFactory<>("priceStr"));
	welderPayCol.setCellValueFactory(new PropertyValueFactory<>("welderStr"));
	painterPayCol.setCellValueFactory(new PropertyValueFactory<>("painterStr"));
	productGroup.getToggles().addAll(railingToggle, benchToggle, swingToggle, capricornToggle);

	productGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
	    reinitProducts(switches.get(newValue));
	});

	switches.put(railingToggle, ProductType.RAILING);
	switches.put(benchToggle, ProductType.BENCH);
	switches.put(swingToggle, ProductType.SWING);
	switches.put(capricornToggle, ProductType.CAPRICORN);

	catalogTable.setItems(products);

	currentPage = 0;

	initCache();

	switchType = ProductType.RAILING;

	productDAO = DataAccessHelper.getDataAccessHelper().getCatalogDAO();

	productGroup.selectToggle(railingToggle);

	updateProducts();
    }

    private void initCache() {
	for (int i = 0; i < max; i++) {
	    productsCache.add(new ProductModel());
	}
    }

    @FXML
    private void productPrevPageAction(ActionEvent event) {
	currentPage--;
	if (currentPage < 0) {
	    currentPage = 0;
	    return;
	}

	updateProducts();
    }

    @FXML
    private void productNextPageAction(ActionEvent event) {

	try {
	    int count = getProductCount();
	    int pages = count / max;
	    if (count - pages * max > 0) {
		pages++;
	    }

	    currentPage++;
	    if (currentPage > pages - 1) {
		currentPage = pages - 1;
		return;
	    }

	    updateProducts();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void productRemoveAction(ActionEvent event) {
	ProductModel item = catalogTable.getSelectionModel().getSelectedItem();
	if (item != null) {
	    try {
		productDAO.deleteProduct(item);
		updateProducts();
	    }
	    catch (SQLException ex) {
		ErrorLog.getErrorLog().show(ex);
	    }
	}
    }

    @FXML
    private void productAddAction(ActionEvent event) {
	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/catalog/add/catalog-add.fxml"), "Каталог кушиш", false, null);
	AddCatalogController acc = (AddCatalogController) container.getController();
	acc.setCatalogOverview(this);
	acc.newModel();
	container.getStage().show();
    }

    @FXML
    private void productEditAction(ActionEvent event) {
	ProductModel item = catalogTable.getSelectionModel().getSelectedItem();
	if (item != null) {
	    WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/catalog/add/catalog-add.fxml"), "Каталог узгартириш", false, null);
	    AddCatalogController acc = (AddCatalogController) container.getController();
	    acc.setCatalogOverview(this);
	    acc.loadModel(item);
	    container.getStage().show();
	}
    }

    private void reinitProducts(ProductType type) {
	this.switchType = type;
	currentPage = 0;
	updateProducts();
    }

    public void handleReinitProducts(ProductType type) {
	switches.forEach((Toggle t, ProductType u) -> {
	    if (u == type) {
		productGroup.selectToggle(t);
                updateProducts();
	    }
	});
    }

    private void updateProducts() {
	if (switchType == null) {
	    return;
	}

	try {
	    productDAO.initProductReader(currentPage, max, switchType.getId());
	    catalogTable.getItems().clear();

	    for (int i = 0; i < max; i++) {
		ProductModel model = productDAO.readProduct(productsCache.get(i));

		if (model == null) {
		    break;
		}
		catalogTable.getItems().add(model);
	    }
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    private int getProductCount() throws SQLException {
	return productDAO.itemsCount(switchType.getId());
    }

}
