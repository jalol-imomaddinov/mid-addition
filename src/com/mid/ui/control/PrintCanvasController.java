/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mid.ui.control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author user
 */
public class PrintCanvasController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane printable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setImage(Image image) {
	imageView.setImage(image);
    }

    public AnchorPane getPrintable() {
	return printable;
    }
}
