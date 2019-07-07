package com.mid.ui.main;

import com.google.gson.Gson;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.mid.app.MainApp;
import com.mid.data.common.AmoundInfo;
import com.mid.data.database.*;
import com.mid.data.model.*;
import com.mid.ui.contract.add.ContractAdd;
import com.mid.ui.contract.find.ContractFindController;
import com.mid.ui.contract.print.ContractPrintController;
import com.mid.ui.contract.tag.ContractTagController;
import com.mid.ui.control.ConfirmerController;
import com.mid.ui.error.ErrorLog;
import com.mid.ui.main.toolbar.ToolbarController;
import com.mid.util.*;
import de.jensd.fx.glyphs.fontawesome.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainController implements Initializable {

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private StackPane rootPane;
    @FXML
    private StackPane content;
    @FXML
    private JFXTextField searchField;

    @FXML
    private TableView<SimpleContractModel> table;
    @FXML
    private TableColumn<SimpleContractModel, Node> selectionCol;
    @FXML
    private TableColumn<SimpleContractModel, Node> colorCol;
    @FXML
    private TableColumn<SimpleContractModel, String> numberCol;
    @FXML
    private TableColumn<SimpleContractModel, String> owner;
    @FXML
    private TableColumn<SimpleContractModel, String> catalogCol;
    @FXML
    private TableColumn<SimpleContractModel, String> sizeCol;
    @FXML
    private TableColumn<SimpleContractModel, String> amoundCol;
    @FXML
    private TableColumn<SimpleContractModel, String> prepaidAmoundCol;
    @FXML
    private TableColumn<SimpleContractModel, String> remaindAmoundCol;
    @FXML
    private TableColumn<SimpleContractModel, String> welderCol;
    @FXML
    private TableColumn<SimpleContractModel, String> painterCol;
    @FXML
    private TableColumn<SimpleContractModel, LocalDate> limitCol;
    @FXML
    private TableColumn<SimpleContractModel, Node> statusCol;
    @FXML
    private JFXButton searchButton;
    @FXML
    private Label agreedLabel;
    @FXML
    private Label prepaidLabel;
    @FXML
    private Label remaindLabel;
    
    private SimpleContractModel lastModel;

    private final FontAwesomeIconView searchMinusIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH_MINUS, "20");
    private final FontAwesomeIconView searchIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH, "20");

    private int currentPage = 0;
    private int beforeFilterPage = 0;

    private final Consumer<SimpleContractModel> stateRefresher = (t) -> {
	table.refresh();
    };

    private ContractDAO contractDAO = DataAccessHelper.getDataAccessHelper().getContractDAO();
    private final ArrayList<Integer> selectedContracts = new ArrayList<>();
    
    private SimpleContractModel.ContractSelectedListener contractSelectedListener;
    private int max = 3;
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private FileChooser gsonChooser = new FileChooser();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
	initDrawer();

	selectionCol.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
	colorCol.setCellValueFactory(new PropertyValueFactory<>("colorCircle"));
	numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
	owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
	catalogCol.setCellValueFactory(new PropertyValueFactory<>("catalog"));
	sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
	amoundCol.setCellValueFactory(new PropertyValueFactory<>("amound"));
	prepaidAmoundCol.setCellValueFactory(new PropertyValueFactory<>("prepaidAmound"));
	remaindAmoundCol.setCellValueFactory(new PropertyValueFactory<>("remaindAmound"));
	welderCol.setCellValueFactory(new PropertyValueFactory<>("welder"));
	painterCol.setCellValueFactory(new PropertyValueFactory<>("painter"));
	limitCol.setCellValueFactory(new PropertyValueFactory<>("limit"));
	statusCol.setCellValueFactory(new PropertyValueFactory<>("stateControl"));

	gsonChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы MID", "*.mmd"));
	
	contractSelectedListener = (SimpleContractModel contract, boolean b) -> {
		if (b)
		    selectedContracts.add(contract.getId());
		else
		    selectedContracts.remove((Integer)contract.getId());
	};
		
	table.heightProperty().addListener((observable, oldValue, newValue) -> {
//	    System.out.println(newValue);
	    int temp_max = (newValue.intValue() / 40);
	    if (temp_max == max) {
		return;
	    }
	    if (temp_max * 40 > newValue.intValue()) {
		temp_max--;
	    }
	    max = temp_max;
	    try {
		updateData();
	    }
	    catch (SQLException ex) {
		ErrorLog.getErrorLog().show(ex);
	    }
	});

	limitCol.setCellFactory((TableColumn<SimpleContractModel, LocalDate> param) -> new TableCell<SimpleContractModel, LocalDate>() {
	    @Override
	    protected void updateItem(LocalDate item, boolean empty) {
		setText(DateUtil.viewFormat(item));
	    }
	});

	remaindAmoundCol.setCellFactory((TableColumn<SimpleContractModel, String> param) -> new TableCell<SimpleContractModel, String>() {
	    @Override
	    protected void updateItem(String item, boolean empty) {
		if (item != null && item.equals("0")) {
		    setText("Расчет");
		}
		else {
		    setText(item);
		}
	    }
	});
	
	searchField.textProperty().addListener((observable, oldValue, newValue) -> {
	    try {
		if (newValue.equals("")) {
		    currentPage = 0;
		    contractDAO.setCriteria(null);
		    updateData();
		}
		else {
		    currentPage = 0;
		    StringBuilder builder = new StringBuilder("WHERE ");
		    builder.append("(hasMatch(owner_name, '")
			    .append(newValue).append("')) and fused = 0 ");
		    contractDAO.setCriteria(builder.toString());
		    updateData();
		}
	    }
	    catch (SQLException ex) {
		throw new RuntimeException(ex);
	    }
	});
    }

    private void initDrawer() {

	URL resource = getClass().getResource("/com/mid/ui/main/toolbar/toolbar.fxml");
	WindowContainer container = MIDUtil.loadFXML(resource);

	ToolbarController controller = (ToolbarController) container.getController();
	VBox toolbar = (VBox) container.getParent();

	drawer.setSidePane(toolbar);

	HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
	task.setRate(-1);

	hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
	    drawer.toggle();
	});

	drawer.setOnDrawerOpening((event) -> {
	    task.setRate(task.getRate() * -1);
	    task.play();
	    drawer.toFront();
	});

	drawer.setOnDrawerClosed((event) -> {
	    drawer.toBack();
	    task.setRate(task.getRate() * -1);
	    task.play();
	});
    }

    public void refreshTable() {
	table.refresh();
	try {
	    updateAmoundInfo();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    public void updateData() throws SQLException {

	long start = 0;
	long end = 0;

	contractDAO.initContractReader(currentPage, max);

	SimpleContractModelFactory.makeContractModels(max);
	ArrayList<SimpleContractModel> models = SimpleContractModelFactory.getContractModels();

	table.getItems().clear();

	for (int i = 0; i < max; i++) {
	    SimpleContractModel model = contractDAO.readSimpleContract(models.get(i));
	    if (model == null) {
		break;
	    }
	    model.setLock(true);
	    model.setContractSelectedListener(contractSelectedListener);
	    model.setSelected(selectedContracts.contains(model.getId()));
	    addItem(model);
	    model.setLock(false);
	}
	updateAmoundInfo();
    }

    private void updateAmoundInfo() throws SQLException {
	AmoundInfo amoundInfo = contractDAO.getAmoundInfo();
	agreedLabel.setText("Сумма: " + StringUtil.toAmound(amoundInfo.getAgreed()));
	prepaidLabel.setText("Туланди: " + StringUtil.toAmound(amoundInfo.getPrepaid()));
	remaindLabel.setText("Остатка: " + StringUtil.toAmound(amoundInfo.getRemaind()));
    }
    
    private void addItem(SimpleContractModel item) {
	table.getItems().add(item);
    }

    @FXML
    private void tableMouseClicked(MouseEvent event) {
	if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
	    editContract();
	}
    }

    @FXML
    private void addContractAction(ActionEvent event) {
	try {
	    WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/contract/add/contract-add.fxml"), "Янги буюртма", false, null);
	    ContractAdd contractAdd = (ContractAdd) container.getController();
	    contractAdd.setContractOverview(this);
	    contractAdd.newModel(contractDAO.nextContractNumber());
	    container.getStage().show();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void editContractAction(ActionEvent event) {
	editContract();
    }

    private void editContract() {
	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/contract/add/contract-add.fxml"), "Буюртмани узгартириш", false, null);
	ContractAdd contractAdd = container.getController();
	contractAdd.setContractOverview(this);

	SimpleContractModel item = getSelectedContractItem();
	if (item != null) {
	    contractAdd.loadModel(item);
	    container.getStage().show();
	}
    }

    private final Consumer<Boolean> removeConsumer = (t) -> {
	final ConfirmerController confirmer = ConfirmerController.getConfirmer();
	confirmer.hide();
	if (t) {
	    try {
		contractDAO.removeContract(lastModel);
		updateData();
	    }
	    catch (SQLException e) {
		ErrorLog.getErrorLog().show(e);
	    }
	}
    };

    @FXML
    private void removeContractAction(ActionEvent event) {
	lastModel = getSelectedContractItem();
	if (lastModel != null) {
	    ConfirmerController confirmer = ConfirmerController.getConfirmer();
	    confirmer.setTitleText("Контрактни учириш");
	    confirmer.setCallback(removeConsumer);

	    StringBuilder builder = new StringBuilder();
	    builder.append("#").append(lastModel.getNumber()).append(" ");
	    builder.append(lastModel.getOwner()).append(" ");
	    builder.append("[").append(lastModel.getCatalog()).append("] ");
	    builder.append("ни учирмокчимисиз?");
	    confirmer.setMessage(builder.toString());
	    confirmer.show();
	}
    }

    @FXML
    private void tagContractAction(ActionEvent event) {
	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/contract/tag/contract-tag.fxml"), "Буюртма теглари", true, null);
	SimpleContractModel item = getSelectedContractItem();
	ContractTagController tagController = (ContractTagController) container.getController();
	tagController.setMainController(this);
	if (item != null) {
	    tagController.setContractModel(item);
	    container.getStage().show();
	}
    }

    private final Consumer<String> findConsumer = (filter) -> {
	setFindFilter(filter);
    };

    @FXML
    private void findContractAction(ActionEvent event) {
	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/contract/find/contract-find.fxml"), "Кидирув", false, null);
	ContractFindController contractFind = container.getController();
	contractFind.setCustomer(searchField.getText());
	contractFind.setCallback(findConsumer);
	container.getStage().show();
    }

    private void clearFindFilter() {
	contractDAO.setCriteria(null);
	currentPage = beforeFilterPage;
	try {
	    updateData();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
	searchButton.setGraphic(searchIcon);
    }

    private void setFindFilter(String filter) {
	contractDAO.setCriteria(filter);
	beforeFilterPage = currentPage;
	currentPage = 0;
	try {
	    updateData();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
	searchButton.setGraphic(searchMinusIcon);
    }

    @FXML
    private void prevPageAction(ActionEvent event) {
	currentPage--;
	if (currentPage < 0) {
	    currentPage = 0;
	    return;
	}

	try {
	    updateData();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void nextPageAction(ActionEvent event) {

	try {
	    int count = contractDAO.getContractCount();
	    int pages = count / max;
	    if (count - pages * max > 0) {
		pages++;
	    }

	    currentPage++;
	    if (currentPage > pages - 1) {
		currentPage = pages - 1;
		return;
	    }

	    updateData();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void printContractAction(ActionEvent event) {

	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/contract/print/contract-print.fxml"), "Чоп этиш", false, null);
	ContractPrintController printController = (ContractPrintController) container.getController();

	SimpleContractModel item = getSelectedContractItem();

	if (item != null) {
	    try {
		printController.setModel(item);
		container.getStage().show();
	    }
	    catch (SQLException ex) {
		ErrorLog.getErrorLog().show(ex);
	    }
	}
    }

    private SimpleContractModel getSelectedContractItem() {
	SimpleContractModel selectedItem = table.getSelectionModel().getSelectedItem();
	return selectedItem;
    }

    public void updateTableItems() {
	table.getItems().clear();
    }

    @FXML
    private void importData(ActionEvent event) {
	FileReader reader = null;
	try {
	    File mmd = gsonChooser.showOpenDialog(MainApp.getPrimaryStage());
	    if (mmd == null) {
		return;
	    }
	    Gson gson = new Gson();
	    reader = new FileReader(mmd);
	    FullContractModel model = gson.fromJson(reader, FullContractModel.class);
	    reader.close();
	    WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/contract/add/contract-add.fxml"), "Буюртмани узгартириш", false, null);
	    ContractAdd contractAdd = container.getController();
	    contractAdd.setContractOverview(this);
	    contractAdd.loadModel(model);
	    container.getStage().show();
	}
	catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @FXML
    private void exportData(ActionEvent event) {
	directoryChooser.setTitle("Експорт в Json");
	File dir = directoryChooser.showDialog(MainApp.getPrimaryStage());
	if (dir == null) {
	    return;
	}
	FullContractModel model = new FullContractModel();
	for (Integer selectedContract : selectedContracts) {
	    try {
		contractDAO.readFullContract(selectedContract, model);
		Gson gson = new Gson();
		try (Writer writer = new FileWriter(new File(dir, model.getNumber() + ".mmd"))) {
		    gson.toJson(model, writer);
		}
	    }
	    catch (SQLException | IOException ex) {
		throw new RuntimeException(ex);
	    }
	}
    }

    @FXML
    private void exportToPDF(ActionEvent event) {
	List<String> commands = new ArrayList<>();
	commands.add(Paths.getExporterPath());
	commands.add("-from_mid");
	for (Integer id : selectedContracts) {
	    commands.add(id.toString());
	}
	ProcessBuilder process = new ProcessBuilder(commands);
	process.redirectError(new File("D:/pdfexporter.err"));
	try {
	    process.start();
	}
	catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }
    
    @FXML
    private void selectAllAction(ActionEvent event) {
	for (SimpleContractModel item : table.getItems()) {
	    item.setSelected(true);
	}
    }  

    @FXML
    private void diselectAction(ActionEvent event) {
	selectedContracts.clear();
	for (SimpleContractModel item : table.getItems()) {
	    item.setSelected(false);
	}
    }    
}