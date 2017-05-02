/*
 * Copyright (C) 2016 Satomichi Nishihara
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

public class QEFXModelerEditorController extends QEFXAppController {

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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupSuperButton();
        this.setupScaleField1();
        this.setupScaleField2();
        this.setupScaleField3();

        this.setupSlabButton();
        this.setupMillerField1();
        this.setupMillerField2();
        this.setupMillerField3();
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
