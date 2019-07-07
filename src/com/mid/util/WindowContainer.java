package com.mid.util;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class WindowContainer {

    private Stage stage;
    private Parent parent;
    private Object controller;

    public WindowContainer(Stage stage, Object controller) {
	this.stage = stage;
	this.controller = controller;
    }

    public WindowContainer(Parent parent, Object controller) {
	this.stage = null;
	this.parent = parent;
	this.controller = controller;
    }

    public WindowContainer(Stage stage, Parent parent, Object controller) {
	this.stage = stage;
	this.parent = parent;
	this.controller = controller;
    }

    public Stage getStage() {
	return stage;
    }

    public void setStage(Stage stage) {
	this.stage = stage;
    }

    public Parent getParent() {
	return parent;
    }

    public void setParent(Parent parent) {
	this.parent = parent;
    }

    public <T> T getController() {
	return (T) controller;
    }

    public void setController(Object controller) {
	this.controller = controller;
    }
}
