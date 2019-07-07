package com.mid.ui.control;

import com.mid.data.common.StateType;
import com.mid.data.model.SimpleContractModel;
import com.mid.util.MIDUtil;
import com.mid.util.WindowContainer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StateControl extends StackPane implements Initializable {

    private final static ArrayList<StateType> STATE_TYPES = new ArrayList<>(Arrays.asList(new StateType[]{
	StateType.BEGIN,
	StateType.PAINT,
	StateType.STETUP,
	StateType.COMPLETE,
	StateType.NONE,})
    );

    public static StateControl getStateControl(SimpleContractModel owner) {

	WindowContainer container = MIDUtil.loadFXML(StateControl.class.getResource("state-control.fxml"));

	Parent pane = container.getParent();
	pane.getStylesheets().add(StateControl.class.getResource("state-control.css").toString());

	StateControl control = container.getController();
	control.setOwner(owner);

	return control;
    }

    @FXML
    private HBox stateBox;
    @FXML
    private HBox subStateBox;
    @FXML
    private ToggleButton runToggle;
    @FXML
    private ToggleButton paintToggle;
    @FXML
    private ToggleButton setupToggle;
    @FXML
    private ToggleButton completeToggle;
//    @FXML
//    private Circle color;
    
    private ToggleGroup group;

    private StateType currentState;

    private SimpleContractModel owner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

	getChildren().add(stateBox);

	group = new ToggleGroup();
	group.getToggles().addAll(runToggle, paintToggle, setupToggle, completeToggle);

	group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
	    int index = group.getToggles().indexOf(newValue);

	    StateType state;

	    if (index == StateType.NONE.getId()) {
		state = StateType.NONE;
	    }
	    else {
		state = StateType.getStateById(index);
	    }

	    if (currentState == state) {
		return;
	    }

	    currentState = state;
	    owner.handleSetState(state);
	});

	for (Toggle toggle : group.getToggles()) {
	    toggle.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
		if (!newValue) {
		    owner.handleSetState(StateType.NONE);
		}
	    });
	}
    }

    public void setOwner(SimpleContractModel owner) {
	this.owner = owner;
    }

    public void setState(StateType state) {
	if (currentState == state) {
	    return;
	}
	currentState = state;

	if (state == StateType.NONE) {
	    Toggle selectedToggle = group.getSelectedToggle();
	    if (selectedToggle != null) {
		selectedToggle.setSelected(false);
	    }
	}
	else {
	    Toggle selected = group.getToggles().get(state.getId());
	    group.selectToggle(selected);
	}
    }
    
    /*    public void setColor(Color color) {
    this.color.setFill(color);
    }*/
    public StateType getState() {
	return currentState;
    }
}
