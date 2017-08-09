/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.designer;

import java.net.URL;
import java.util.ResourceBundle;

import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.app.project.editor.input.items.QEFXItem;
import burai.app.project.viewer.designer.QEFXDesignerViewer;
import burai.atoms.design.AtomsStyle;
import burai.atoms.design.Design;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;
import burai.com.keys.KeyNames;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

public class QEFXDesignerEditorController extends QEFXAppController {

    private static final double CTRL_GRAPHIC_SIZE = 20.0;
    private static final String CTRL_GRAPHIC_CLASS = "piclight-button";

    private QEFXProjectController projectController;

    private QEFXDesignerViewer viewer;

    private Design design;

    @FXML
    private Button undoButton;

    @FXML
    private Label undoLabel;

    @FXML
    private Button redoButton;

    @FXML
    private Label redoLabel;

    @FXML
    private Button centerButton;

    @FXML
    private Label centerLabel;

    @FXML
    private ComboBox<AtomsStyle> styleCombo;

    @FXML
    private Button styleButton;

    @FXML
    private ColorPicker backColorPicker;

    @FXML
    private Button backButton;

    @FXML
    private ColorPicker fontColorPicker;

    @FXML
    private Button fontButton;

    @FXML
    private ToggleButton legendToggle;

    @FXML
    private Button legendButton;

    @FXML
    private ToggleButton axisToggle;

    @FXML
    private Button axisButton;

    public QEFXDesignerEditorController(QEFXProjectController projectController, QEFXDesignerViewer viewer) {
        super(projectController == null ? null : projectController.getMainController());

        if (viewer == null) {
            throw new IllegalArgumentException("viewer is null.");
        }

        this.projectController = projectController;

        this.viewer = viewer;
        this.design = viewer.getDesign();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupUndoButton();
        this.setupUndoLabel();
        this.setupRedoButton();
        this.setupRedoLabel();
        this.setupCenterButton();
        this.setupCenterLabel();

        this.setupAtomsStyle();
        this.setupBackColor();
        this.setupFontColor();
        this.setupShowLegend();
        this.setupShowAxis();

        // TODO
    }

    private void setupUndoButton() {
        if (this.undoButton == null) {
            return;
        }

        this.undoButton.setText("");
        this.undoButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.UNDO, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.undoButton.setOnAction(event -> {
            // TODO
        });
    }

    private void setupUndoLabel() {
        if (this.undoLabel == null) {
            return;
        }

        String text = this.undoLabel.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        text = text.replaceAll("Shortcut", KeyNames.getShortcut());
        this.undoLabel.setText(text);
    }

    private void setupRedoButton() {
        if (this.redoButton == null) {
            return;
        }

        this.redoButton.setText("");
        this.redoButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.REDO, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.redoButton.setOnAction(event -> {
            // TODO
        });
    }

    private void setupRedoLabel() {
        if (this.redoLabel == null) {
            return;
        }

        String text = this.redoLabel.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        text = text.replaceAll("Shortcut", KeyNames.getShortcut());
        this.redoLabel.setText(text);
    }

    private void setupCenterButton() {
        if (this.centerButton == null) {
            return;
        }

        this.centerButton.setText("");
        this.centerButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CENTER, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.centerButton.setOnAction(event -> {
            if (this.viewer != null) {
                this.viewer.centerAtomsViewer();
            }
        });
    }

    private void setupCenterLabel() {
        if (this.centerLabel == null) {
            return;
        }

        String text = this.centerLabel.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        text = text.replaceAll("Shortcut", KeyNames.getShortcut());
        this.centerLabel.setText(text);
    }

    private void setupAtomsStyle() {
        if (this.styleCombo == null) {
            return;
        }

        this.styleCombo.getItems().clear();
        this.styleCombo.getItems().addAll(AtomsStyle.values());
        this.styleCombo.setValue(this.design == null ? null : this.design.getAtomsStyle());
        this.styleCombo.setOnAction(event -> {
            AtomsStyle atomsStyle = this.styleCombo.getValue();
            if (this.design != null && atomsStyle != null) {
                this.design.setAtomsStyle(atomsStyle);
            }
        });

        if (this.styleButton != null) {
            QEFXItem.setupDefaultButton(this.styleButton);
            this.styleButton.setOnAction(event -> {
                this.styleCombo.setValue(AtomsStyle.BALL_STICK);
            });
        }
    }

    private void setupBackColor() {
        if (this.backColorPicker == null) {
            return;
        }

        this.backColorPicker.setValue(this.design == null ? null : this.design.getBackColor());
        this.backColorPicker.valueProperty().addListener(o -> {
            Color color = this.backColorPicker.getValue();
            if (this.design != null && color != null) {
                this.design.setBackColor(color);
            }
        });

        if (this.backButton != null) {
            QEFXItem.setupDefaultButton(this.backButton);
            this.backButton.setOnAction(event -> {
                this.backColorPicker.setValue(Color.DIMGRAY);
            });
        }
    }

    private void setupFontColor() {
        if (this.fontColorPicker == null) {
            return;
        }

        this.fontColorPicker.setValue(this.design == null ? null : this.design.getFontColor());
        this.fontColorPicker.valueProperty().addListener(o -> {
            Color color = this.fontColorPicker.getValue();
            if (this.design != null && color != null) {
                this.design.setFontColor(color);
            }
        });

        if (this.fontButton != null) {
            QEFXItem.setupDefaultButton(this.fontButton);
            this.fontButton.setOnAction(event -> {
                this.fontColorPicker.setValue(Color.BLACK);
            });
        }
    }

    private void setupShowLegend() {
        // TODO
    }

    private void setupShowAxis() {
        // TODO
    }

}
