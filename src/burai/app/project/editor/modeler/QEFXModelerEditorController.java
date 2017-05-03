/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.modeler;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class QEFXModelerEditorController extends QEFXAppController {

    private static final double GRAPHIC_SIZE = 20.0;
    private static final String GRAPHIC_CLASS = "piclight-button";

    protected QEFXProjectController projectController;

    @FXML
    private Button screenButton;

    @FXML
    private Button superButton;

    @FXML
    private TextField scaleField1;

    @FXML
    private TextField scaleField2;

    @FXML
    private TextField scaleField3;

    @FXML
    private Button slabButton;

    @FXML
    private TextField millerField1;

    @FXML
    private TextField millerField2;

    @FXML
    private TextField millerField3;

    @FXML
    private ToggleButton symbolToggle;

    @FXML
    private ComboBox<Double> widthCombo;

    @FXML
    private ComboBox<Integer> typeCombo;

    public QEFXModelerEditorController(QEFXProjectController projectController) {
        super(projectController == null ? null : projectController.getMainController());
        this.projectController = projectController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupScreenButton();

        this.setupSuperButton();
        this.setupScaleField1();
        this.setupScaleField2();
        this.setupScaleField3();

        this.setupSlabButton();
        this.setupMillerField1();
        this.setupMillerField2();
        this.setupMillerField3();
    }

    private void setupScreenButton() {
        if (this.screenButton == null) {
            return;
        }

        this.screenButton.setText("");
        this.screenButton.setGraphic(SVGLibrary.getGraphic(SVGData.CAMERA, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.screenButton.setOnAction(event -> {
            if (this.projectController != null) {
                this.projectController.sceenShot();
            }
        });
    }

    private void setupSuperButton() {
        if (this.superButton == null) {
            return;
        }
        // TODO
    }

    private void setupScaleField1() {
        if (this.scaleField1 == null) {
            return;
        }
        // TODO
    }

    private void setupScaleField2() {
        if (this.scaleField2 == null) {
            return;
        }
        // TODO
    }

    private void setupScaleField3() {
        if (this.scaleField3 == null) {
            return;
        }
        // TODO
    }

    private void setupSlabButton() {
        if (this.slabButton == null) {
            return;
        }
        // TODO
    }

    private void setupMillerField1() {
        if (this.millerField1 == null) {
            return;
        }
        // TODO
    }

    private void setupMillerField2() {
        if (this.millerField2 == null) {
            return;
        }
        // TODO
    }

    private void setupMillerField3() {
        if (this.millerField3 == null) {
            return;
        }
        // TODO
    }

}
