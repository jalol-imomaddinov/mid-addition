package com.mid.ui.control;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.ToggleButton;

/**
 * @author
 */
public class EditableButton {

    private final ToggleButton toggleButton;
    private final FontAwesomeIconView iconView;

    public EditableButton(ToggleButton toggleButton) {
	this.toggleButton = toggleButton;
	this.iconView = (FontAwesomeIconView) toggleButton.getGraphic();
	this.iconView.setIcon(FontAwesomeIcon.EDIT);

	this.toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
	    if (newValue) {
		editable();
	    }
	    else {
		notEditable();
	    }
	});
    }

    private void editable() {
	iconView.setIcon(FontAwesomeIcon.EDIT);
    }

    private void notEditable() {
	iconView.setIcon(FontAwesomeIcon.BINOCULARS);
    }
}
