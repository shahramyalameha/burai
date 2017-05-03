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
import burai.app.project.viewer.modeler.Modeler;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class QEFXModelerEditorController extends QEFXAppController {

    private static final double CTRL_GRAPHIC_SIZE = 20.0;
    private static final String CTRL_GRAPHIC_CLASS = "piclight-button";

    private static final double BUILD_GRAPHIC_SIZE = 20.0;
    private static final String BUILD_GRAPHIC_CLASS = "piclight-button";

    private QEFXProjectController projectController;

    private Modeler modeler;

    @FXML
    private Button screenButton;

    @FXML
    private Button reflectButton;

    @FXML
    private Button initButton;

    @FXML
    private Button undoButton;

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

    public QEFXModelerEditorController(QEFXProjectController projectController, Modeler modeler) {
        super(projectController == null ? null : projectController.getMainController());

        if (modeler == null) {
            throw new IllegalArgumentException("modeler is null.");
        }

        this.projectController = projectController;
        this.modeler = modeler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupScreenButton();
        this.setupReflectButton();
        this.setupInitButton();
        this.setupUndoButton();

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
        this.screenButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CAMERA, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.screenButton.setOnAction(event -> {
            if (this.projectController != null) {
                this.projectController.sceenShot();
            }
        });
    }

    private void setupReflectButton() {
        if (this.reflectButton == null) {
            return;
        }

        this.reflectButton.setText("");
        this.reflectButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.OUT, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.reflectButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.reflect();
            }
        });
    }

    private void setupInitButton() {
        if (this.initButton == null) {
            return;
        }

        this.initButton.setText("");
        this.initButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.INTO, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.initButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.initialize();
            }
        });
    }

    private void setupUndoButton() {
        if (this.undoButton == null) {
            return;
        }

        this.undoButton.setText("");
        this.undoButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.UNDO, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.undoButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.undo();
            }
        });
    }

    private void setupSuperButton() {
        if (this.superButton == null) {
            return;
        }

        this.superButton.getStyleClass().add(BUILD_GRAPHIC_CLASS);

        this.superButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.MODELER, BUILD_GRAPHIC_SIZE, null, BUILD_GRAPHIC_CLASS));

        String text = this.superButton.getText();
        if (text != null) {
            this.superButton.setText(text + " ");
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

        this.slabButton.getStyleClass().add(BUILD_GRAPHIC_CLASS);

        this.slabButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.MODELER, BUILD_GRAPHIC_SIZE, null, BUILD_GRAPHIC_CLASS));

        String text = this.slabButton.getText();
        if (text != null) {
            this.slabButton.setText(text + " ");
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
