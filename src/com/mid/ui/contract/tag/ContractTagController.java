package com.mid.ui.contract.tag;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.mid.data.common.JobType;
import com.mid.data.database.ContractDAO;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.PayementDAO;
import com.mid.data.database.WorkerDAO;
import com.mid.data.model.PayementModel;
import com.mid.data.model.SimpleContractModel;
import com.mid.data.model.WorkerModel;
import com.mid.ui.control.ControlUtil;
import com.mid.ui.control.IllegalFieldValueException;
import com.mid.ui.control.converter.AmoundConverter;
import com.mid.ui.error.ErrorLog;
import com.mid.ui.main.MainController;
import com.mid.util.StringUtil;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ContractTagController implements Initializable {

    @FXML
    private JFXListView<WorkerCheckBox> weldersList;
    @FXML
    private JFXListView<WorkerCheckBox> paintersList;

    @FXML
    private TableView<PayementModel> paysTable;
    @FXML
    private TableColumn<PayementModel, LocalDate> dateCol;
    @FXML
    private TableColumn<PayementModel, String> amoundCol;
    @FXML
    private JFXTextField amoundField;
    @FXML
    private Label ownerName;
    @FXML
    private Label welderPay;
    @FXML
    private Label painterPay;
    @FXML
    private AnchorPane checkPane;

    private int id;

    private ObservableList<WorkerModel> workers;

    private ObservableList<WorkerCheckBox> checkBoxes;

    private WorkerDAO workerDAO;
    private ContractDAO cdao;

    private SimpleContractModel contractModel;

    private MainController mainController;

    private double painter_pay;
    private double welder_pay;

    private double current_welder_pay;
    private double current_painter_pay;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	checkBoxes = FXCollections.observableArrayList();

	dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
	amoundCol.setCellValueFactory(new PropertyValueFactory<>("amound"));

	amoundField.setTextFormatter(new TextFormatter<>(new AmoundConverter(null)));

	workerDAO = DataAccessHelper.getDataAccessHelper().getWorkerDAO();
	cdao = DataAccessHelper.getDataAccessHelper().getContractDAO();

	try {
	    workers = workerDAO.getWorkers();
	    workers.addListener((ListChangeListener.Change<? extends WorkerModel> c) -> {
		initWorkerLists();
	    });

	    initWorkerLists();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    public void setContractModel(SimpleContractModel model) {

	this.id = model.getId();

	this.contractModel = model;
	this.ownerName.setText("#" + model.getNumber() + " " + contractModel.getOwner());

	try {

	    double[] pays = cdao.getWorkersPay(id);

	    welder_pay = pays[0];
	    painter_pay = pays[1];

	    List<Integer> bindedWorkers = workerDAO.bindedWorkers(id);

	    for (WorkerCheckBox checkBox : checkBoxes) {

		checkBox.setSelected(false);

		for (Integer i : bindedWorkers) {
		    if (i == checkBox.getWorkerId()) {
			checkBox.setSelected(true);
		    }
		}
	    }

	    welderPay.setText(StringUtil.toAmound(welder_pay));
	    painterPay.setText(StringUtil.toAmound(painter_pay));

	    refreshPayements();

	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    public void setMainController(MainController mainController) {
	this.mainController = mainController;
    }

    private void initWorkerLists() {

	weldersList.getItems().clear();
	paintersList.getItems().clear();

	for (WorkerModel worker : workers) {
	    if (!worker.isFired()) {

		WorkerCheckBox wcb;
		if (worker.getJobType() == JobType.PAINTER) {
		    wcb = new WorkerCheckBox(worker.getId(), worker.getName(), worker.getJobType());
		    paintersList.getItems().add(wcb);
		}
		else {
		    wcb = new WorkerCheckBox(worker.getId(), worker.getName(), worker.getJobType());
		    weldersList.getItems().add(wcb);
		}
		wcb.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
		    updatePays.accept(newValue);
		});
		checkBoxes.add(wcb);
	    }
	}
    }

    @FXML
    private void bindWorkersAction(ActionEvent event) {
	try {
	    long time = System.currentTimeMillis();

	    workerDAO.unbindWorkers(id);

	    for (WorkerCheckBox box : paintersList.getItems()) {
		if (box.isSelected()) {
		    workerDAO.bindWorker(box.workerId, id);
		}
	    }
	    for (WorkerCheckBox box : weldersList.getItems()) {
		if (box.isSelected()) {
		    workerDAO.bindWorker(box.workerId, id);
		}
	    }

	    refreshItem();
	    showCheck();

	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

/*    @FXML
    private void sendToPaychack(ActionEvent event) {
	try {
	    long time = System.currentTimeMillis();

	    workerDAO.removeSendedPaychacks(id);

	    for (WorkerCheckBox box : paintersList.getItems()) {
		if (box.isSelected()) {
		    workerDAO.addPaychack(id, box.workerId, 1, current_painter_pay, ownerName.getText(), LocalDate.now());
		}
	    }
	    for (WorkerCheckBox box : weldersList.getItems()) {
		if (box.isSelected()) {
		    workerDAO.addPaychack(id, box.workerId, 1, current_welder_pay, ownerName.getText(), LocalDate.now());
		}
	    }

	    showCheck();
	    System.out.println("send time: " + (System.currentTimeMillis() - time));

	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }
*/
    @FXML
    private void addPayement(ActionEvent event) {
	try {
	    double amound = ControlUtil.getFieldAmound(amoundField);
	    LocalDate date = LocalDate.now();

	    PayementDAO pdao = DataAccessHelper.getDataAccessHelper().getPayementDAO();
	    pdao.addPayement(id, date, amound);

	    refreshPayements();
	    refreshItem();

	}
	catch (IllegalFieldValueException ex) {

	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void removePayement(ActionEvent event) {
	PayementModel selectedItem = paysTable.getSelectionModel().getSelectedItem();
	if (selectedItem != null) {
	    try {
		PayementDAO pdao = DataAccessHelper.getDataAccessHelper().getPayementDAO();
		pdao.removePayement(selectedItem.getId());
		refreshPayements();
		refreshItem();
	    }
	    catch (SQLException ex) {
		ErrorLog.getErrorLog().show(ex);
	    }
	}
    }

    private void refreshPayements() throws SQLException {
	PayementDAO pdao = DataAccessHelper.getDataAccessHelper().getPayementDAO();
	List<PayementModel> payements = pdao.getPayements(id);

	paysTable.getItems().clear();
	for (PayementModel payement : payements) {
	    paysTable.getItems().add(payement);
	}
    }

    private void refreshItem() throws SQLException {
	ContractDAO cdao = DataAccessHelper.getDataAccessHelper().getContractDAO();
	cdao.refreshSimpleContract(contractModel);
	mainController.refreshTable();
    }

    private final Consumer updatePays = t -> {

	int wc = 0;
	int pc = 0;

	for (WorkerCheckBox checkBox : checkBoxes) {
	    if (checkBox.isSelected()) {
		if (checkBox.getJobType() == JobType.PAINTER) {
		    pc++;
		}
		else {
		    wc++;
		}
	    }
	}

	if (wc == 0) {
	    wc = 1;
	}
	if (pc == 0) {
	    pc = 1;
	}

	current_welder_pay = welder_pay / wc;
	current_painter_pay = painter_pay / pc;

	welderPay.setText(StringUtil.toAmound(welder_pay / wc));
	painterPay.setText(StringUtil.toAmound(painter_pay / pc));
    };

    @FXML
    private void hideCheck(MouseEvent event) {
	checkPane.setVisible(false);
    }

    private void showCheck() {
	checkPane.setVisible(true);
    }

    private static class WorkerCheckBox extends JFXCheckBox {

	private final int workerId;
	private final JobType jobType;

	public WorkerCheckBox(int id, String text, JobType type) {
	    super(text);
	    workerId = id;
	    this.jobType = type;
	    setStyle("-fx-text-fill: black;");
	}

	public JobType getJobType() {
	    return jobType;
	}

	public int getWorkerId() {
	    return workerId;
	}
    }
}
