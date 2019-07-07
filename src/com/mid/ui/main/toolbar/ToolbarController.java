package com.mid.ui.main.toolbar;

import com.mid.data.database.DataAccessHelper;
import com.mid.ui.contract.add.ContractAdd;
import com.mid.ui.error.ErrorLog;
import com.mid.ui.help.HelpController;
import com.mid.ui.workers.WorkersController;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ToolbarController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void loadAddContract(ActionEvent event) {
	try {
	    WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/contract/add/contract-add.fxml"), "Add Contract", false, null);
	    ContractAdd contractAdd = (ContractAdd) container.getController();
	    contractAdd.newModel(DataAccessHelper.getDataAccessHelper().getContractDAO().nextContractNumber());
	    container.getStage().show();
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    @FXML
    private void loadWorkers(ActionEvent event) {
	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/workers/workers.fxml"), "Ишчилар", true, null);
	WorkersController workers = (WorkersController) container.getController();
	workers.updateDates();
    }

    @FXML
    private void loadCatalog(ActionEvent event) {
	MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/catalog/catalog-overview.fxml"), "Каталог", true, null);
    }

    @FXML
    private void loadSettings(ActionEvent event) {
	MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/settings/settings.fxml"), "Параметрлар", true, null);
    }

    @FXML
    private void loadHelp(ActionEvent event) {
	WindowContainer container = MIDUtil.loadWindow(getClass().getResource("/com/mid/ui/help/help.fxml"), "Ёрдам", false, null);
	HelpController hc = container.getController();
	hc.initStyles();
	container.getStage().show();
    }
}
