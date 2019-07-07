package com.mid.data.model;

import com.jfoenix.controls.JFXCheckBox;
import com.mid.data.common.ProductType;
import com.mid.data.common.StateType;
import com.mid.data.database.ContractDAO;
import com.mid.data.database.DataAccessHelper;
import com.mid.ui.control.StateControl;
import com.mid.ui.error.ErrorLog;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SimpleContractModel {

    
    private int id;

    private Circle colorCircle;
    
    private int number;

    private String owner;

    private String catalog;

    private String size;

    private String amound;

    private String prepaidAmound;

    private String remaindAmound;

    private String welder;

    private String painter;

    private LocalDate limit;

    private StateType state;

    private StateControl stateControl;
    
    private JFXCheckBox checkBox;

    private ContractDAO contractDAO;

    private boolean lockWhileLoading = false;
    
    private int index;
    
    private boolean lock;
    
    private ContractSelectedListener contractSelectedListener;
    
    private ProductType type;
    
    public SimpleContractModel(int index) {
	this.index = index;
	stateControl = StateControl.getStateControl(this);

	checkBox = new JFXCheckBox();
	checkBox.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	checkBox.setMinWidth(checkBox.getHeight());
	checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    if (contractSelectedListener != null) {
		contractSelectedListener.contractSelected(this, newValue);
	    }
	});
	
	colorCircle = new Circle(10);
	colorCircle.setStrokeWidth(0);
	colorCircle.setFill(Color.BLACK);

	contractDAO = DataAccessHelper.getDataAccessHelper().getContractDAO();
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public void setNumber(int number) {
	this.number = number;
    }

    public int getNumber() {
	return number;
    }

    public void setColorCircle(Circle colorCircle) {
	this.colorCircle = colorCircle;
    }

    public Circle getColorCircle() {
	return colorCircle;
    }

    public JFXCheckBox getCheckBox() {
	return checkBox;
    }

    public String getOwner() {
	return owner;
    }

    public void setOwner(String owner) {
	this.owner = owner;
    }

    public String getCatalog() {
	return catalog;
    }

    public void setCatalog(String catalog) {
	this.catalog = catalog;
    }

    public String getSize() {
	return size;
    }

    public void setSize(String size) {
	this.size = size;
    }

    public String getAmound() {
	return amound;
    }

    public void setAmound(String amound) {
	this.amound = amound;
    }

    public String getPrepaidAmound() {
	return prepaidAmound;
    }

    public void setPrepaidAmound(String prepaidAmound) {
	this.prepaidAmound = prepaidAmound;
    }

    public String getRemaindAmound() {
	return remaindAmound;
    }

    public void setRemaindAmound(String remaindAmound) {
	this.remaindAmound = remaindAmound;
    }

    public String getWelder() {
	return welder;
    }

    public void setWelder(String welder) {
	this.welder = welder;
    }

    public String getPainter() {
	return painter;
    }

    public void setPainter(String painter) {
	this.painter = painter;
    }

    public void setLimit(LocalDate limit) {
	this.limit = limit;
    }

    public LocalDate getLimit() {
	return limit;
    }

    public void setState(StateType state) {
	this.state = state;
	this.stateControl.setState(state);
    }

    public StateType getState() {
	return state;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public void handleSetState(StateType state) {
	if (lockWhileLoading) {
	    return;
	}
	this.state = state;
	this.initStateColor();
	try {
	    contractDAO.updateState(this, state);
	}
	catch (SQLException ex) {
	    ErrorLog.getErrorLog().show(ex);
	}
    }

    public StateControl getStateControl() {
	return stateControl;
    }

    public void setStateRefresher(Consumer<SimpleContractModel> stateRefresher) {
    }
    
    public void initStateColor() {
	Color color;
	
	color = Color.web("#99ccff");
	if (state == StateType.COMPLETE) {
	    color = Color.web("#66e98f");
	}
	else {
	    LocalDate minusDate = getLimit().minusDays(6);
	    LocalDate nowDate = LocalDate.now();

	    if (getLimit().isBefore(nowDate)) {
		color = Color.web("#ff6666");
	    }
	    else if (minusDate.isBefore(nowDate) || minusDate.isEqual(nowDate)) {
		color = Color.web("#ffcc66");
	    }
	}
	
	this.colorCircle.setFill(color);
    }

    public void lockWhileLoading(boolean lockWhileLoading) {
	this.lockWhileLoading = lockWhileLoading;
    }
    
    public void setSelected(boolean b) {
	checkBox.setSelected(b);
    }

    public boolean isSelected() {
	return checkBox.isSelected();
    }

    public void setLock(boolean lock) {
	this.lock = lock;
    }

    public void setContractSelectedListener(ContractSelectedListener contractSelectedListener) {
	this.contractSelectedListener = contractSelectedListener;
    }

    public static interface ContractSelectedListener {
	public void contractSelected(SimpleContractModel contract, boolean b);
    }
}