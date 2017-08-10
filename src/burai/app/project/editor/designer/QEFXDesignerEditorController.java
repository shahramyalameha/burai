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
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.app.project.editor.input.items.QEFXItem;
import burai.app.project.viewer.designer.QEFXDesignerViewer;
import burai.atoms.design.AtomDesign;
import burai.atoms.design.AtomsStyle;
import burai.atoms.design.Design;
import burai.atoms.element.ElementUtil;
import burai.com.graphic.ToggleGraphics;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;
import burai.com.keys.KeyNames;
import burai.com.periodic.ElementButton;
import burai.com.periodic.PeriodicTable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

public class QEFXDesignerEditorController extends QEFXAppController {

    private static final double CTRL_GRAPHIC_SIZE = 20.0;
    private static final String CTRL_GRAPHIC_CLASS = "piclight-button";

    private static final String TOGGLE_STYLE = "-fx-base: transparent";
    private static final String TOGGLE_STYLE_YES = "toggle-graphic-on";
    private static final String TOGGLE_STYLE_NO = "toggle-graphic-off";
    private static final String TOGGLE_TEXT_YES = "yes";
    private static final String TOGGLE_TEXT_NO = "no";
    private static final double TOGGLE_WIDTH = 185.0;
    private static final double TOGGLE_HEIGHT = 24.0;

    private static final String ELEMENT_EMPTY_TEXT = "no element";

    private static final String ERROR_STYLE = QEFXItem.ERROR_STYLE;

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
    private Button backColorButton;

    @FXML
    private ColorPicker fontColorPicker;

    @FXML
    private Button fontColorButton;

    @FXML
    private ToggleButton legendToggle;

    @FXML
    private Button legendButton;

    @FXML
    private ToggleButton axisToggle;

    @FXML
    private Button axisButton;

    private String elemName;

    @FXML
    private Button elemButton;

    @FXML
    private ColorPicker atomColorPicker;

    @FXML
    private Button atomColorButton;

    @FXML
    private TextField atomRadiusField;

    @FXML
    private Button atomRadiusButton;

    @FXML
    private TextField bondWidthField;

    @FXML
    private Button bondWidthButton;

    @FXML
    private ToggleButton cellToggle;

    @FXML
    private Button cellButton;

    @FXML
    private ColorPicker cellColorPicker;

    @FXML
    private Button cellColorButton;

    @FXML
    private TextField cellWidthField;

    @FXML
    private Button cellWidthButton;

    public QEFXDesignerEditorController(QEFXProjectController projectController, QEFXDesignerViewer viewer) {
        super(projectController == null ? null : projectController.getMainController());

        if (viewer == null) {
            throw new IllegalArgumentException("viewer is null.");
        }

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

        this.setupElement();
        this.setupAtomColor();
        this.setupAtomRadius();

        this.setupBondWidth();

        this.setupShowCell();
        this.setupCellColor();
        this.setupCellWidth();
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

        if (this.backColorButton != null) {
            QEFXItem.setupDefaultButton(this.backColorButton);
            this.backColorButton.setOnAction(event -> {
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

        if (this.fontColorButton != null) {
            QEFXItem.setupDefaultButton(this.fontColorButton);
            this.fontColorButton.setOnAction(event -> {
                this.fontColorPicker.setValue(Color.BLACK);
            });
        }
    }

    private void setupShowLegend() {
        if (this.legendToggle == null) {
            return;
        }

        this.legendToggle.setText("");
        this.legendToggle.setStyle(TOGGLE_STYLE);
        this.legendToggle.setSelected(this.design == null ? false : this.design.isShowingLegend());
        this.updateToggleGraphics(this.legendToggle);
        this.legendToggle.selectedProperty().addListener(o -> {
            this.updateToggleGraphics(this.legendToggle);
            if (this.design != null) {
                this.design.setShowingLegend(this.legendToggle.isSelected());
            }
        });

        if (this.legendButton != null) {
            QEFXItem.setupDefaultButton(this.legendButton);
            this.legendButton.setOnAction(event -> {
                this.legendToggle.setSelected(true);
            });
        }
    }

    private void setupShowAxis() {
        if (this.axisToggle == null) {
            return;
        }

        this.axisToggle.setText("");
        this.axisToggle.setStyle(TOGGLE_STYLE);
        this.axisToggle.setSelected(this.design == null ? false : this.design.isShowingAxis());
        this.updateToggleGraphics(this.axisToggle);
        this.axisToggle.selectedProperty().addListener(o -> {
            this.updateToggleGraphics(this.axisToggle);
            if (this.design != null) {
                this.design.setShowingAxis(this.axisToggle.isSelected());
            }
        });

        if (this.axisButton != null) {
            QEFXItem.setupDefaultButton(this.axisButton);
            this.axisButton.setOnAction(event -> {
                this.axisToggle.setSelected(true);
            });
        }
    }

    private void setupElement() {
        this.elemName = null;

        if (this.elemButton == null) {
            return;
        }

        String text = null;
        String[] names = this.design.namesOfAtoms();
        if (names != null && names.length > 0) {
            Arrays.sort(names);
            text = ElementUtil.toElementName(names[0]);
        }
        if (text != null) {
            text = text.trim();
        }
        if (text == null || text.isEmpty()) {
            text = ELEMENT_EMPTY_TEXT;
        }

        this.elemButton.setText(text);
        this.updateElemName();

        this.elemButton.setOnAction(event -> {
            PeriodicTable periodicTable = new PeriodicTable();
            Optional<ElementButton> optElement = periodicTable.showAndWait();
            if (optElement == null || !optElement.isPresent()) {
                return;
            }

            ElementButton element = optElement.get();
            String name = element.getName();
            if (name != null) {
                name = name.trim();
            }

            if (name != null && !name.isEmpty()) {
                this.elemButton.setText(name);
                this.updateElemName();
            }
        });
    }

    private void updateElemName() {
        this.elemName = null;

        String text = this.elemButton == null ? null : this.elemButton.getText();
        if (text == null) {
            return;
        }

        text = text.trim();
        if (text.isEmpty()) {
            return;
        }

        if (ELEMENT_EMPTY_TEXT.equals(text)) {
            return;
        }

        this.elemName = text;
    }

    private AtomDesign getAtomDesign() {
        if (this.design == null || this.elemName == null) {
            return null;
        }

        return this.design.getAtomDesign(this.elemName);
    }

    private void setupAtomColor() {
        if (this.atomColorPicker == null) {
            return;
        }

        AtomDesign atomDesign = this.getAtomDesign();
        this.atomColorPicker.setValue(atomDesign == null ? null : atomDesign.getColor());

        this.atomColorPicker.valueProperty().addListener(o -> {
            AtomDesign atomDesign_ = this.getAtomDesign();
            if (atomDesign_ == null) {
                return;
            }

            Color color = this.atomColorPicker.getValue();
            if (color != null) {
                atomDesign_.setColor(color);
            }
        });

        if (this.atomColorButton != null) {
            QEFXItem.setupDefaultButton(this.atomColorButton);
            this.atomColorButton.setOnAction(event -> {
                if (this.elemName != null) {
                    this.atomColorPicker.setValue(ElementUtil.getColor(this.elemName));
                }
            });
        }
    }

    private void setupAtomRadius() {
        if (this.atomRadiusField == null) {
            return;
        }

        // TODO
    }

    private void setupBondWidth() {
        if (this.bondWidthField == null) {
            return;
        }

        if (this.design != null) {
            double value = this.design.getBondWidth();
            this.bondWidthField.setText(Double.toString(value));
            this.setFieldStyle(this.bondWidthField, value);
        } else {
            this.bondWidthField.setText("");
            this.setFieldStyle(this.bondWidthField, -1.0);
        }

        this.cellWidthField.textProperty().addListener(o -> {
            double value = this.getFieldValue(this.bondWidthField);
            this.setFieldStyle(this.bondWidthField, value);

            if (value > 0.0) {
                if (this.design != null) {
                    this.design.setBondWidth(value);
                }
            }
        });

        if (this.bondWidthButton != null) {
            QEFXItem.setupDefaultButton(this.bondWidthButton);
            this.bondWidthButton.setOnAction(event -> {
                this.bondWidthField.setText("1.0");
            });
        }
    }

    private void setupShowCell() {
        if (this.cellToggle == null) {
            return;
        }

        this.cellToggle.setText("");
        this.cellToggle.setStyle(TOGGLE_STYLE);
        this.cellToggle.setSelected(this.design == null ? false : this.design.isShowingCell());
        this.updateToggleGraphics(this.cellToggle);
        this.cellToggle.selectedProperty().addListener(o -> {
            this.updateToggleGraphics(this.cellToggle);
            if (this.design != null) {
                this.design.setShowingCell(this.cellToggle.isSelected());
            }
        });

        if (this.cellButton != null) {
            QEFXItem.setupDefaultButton(this.cellButton);
            this.cellButton.setOnAction(event -> {
                this.cellToggle.setSelected(true);
            });
        }
    }

    private void setupCellColor() {
        if (this.cellColorPicker == null) {
            return;
        }

        this.cellColorPicker.setValue(this.design == null ? null : this.design.getCellColor());
        this.cellColorPicker.valueProperty().addListener(o -> {
            Color color = this.cellColorPicker.getValue();
            if (this.design != null && color != null) {
                this.design.setCellColor(color);
            }
        });

        if (this.cellColorButton != null) {
            QEFXItem.setupDefaultButton(this.cellColorButton);
            this.cellColorButton.setOnAction(event -> {
                this.cellColorPicker.setValue(Color.BLACK);
            });
        }
    }

    private void setupCellWidth() {
        if (this.cellWidthField == null) {
            return;
        }

        if (this.design != null) {
            double value = this.design.getCellWidth();
            this.cellWidthField.setText(Double.toString(value));
            this.setFieldStyle(this.cellWidthField, value);
        } else {
            this.cellWidthField.setText("");
            this.setFieldStyle(this.cellWidthField, -1.0);
        }

        this.cellWidthField.textProperty().addListener(o -> {
            double value = this.getFieldValue(this.cellWidthField);
            this.setFieldStyle(this.cellWidthField, value);

            if (value > 0.0) {
                if (this.design != null) {
                    this.design.setCellWidth(value);
                }
            }
        });

        if (this.cellWidthButton != null) {
            QEFXItem.setupDefaultButton(this.cellWidthButton);
            this.cellWidthButton.setOnAction(event -> {
                this.cellWidthField.setText("1.0");
            });
        }
    }

    private double getFieldValue(TextField textField) {
        if (textField == null) {
            return -1.0;
        }

        String text = textField.getText();
        if (text == null) {
            return -1.0;
        }

        text = text.trim();
        if (text.isEmpty()) {
            return -1.0;
        }

        double value = -1.0;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            value = -1.0;
        }

        return value;
    }

    private void setFieldStyle(TextField textField, double value) {
        if (textField == null) {
            return;
        }

        if (value > 0.0) {
            textField.setStyle(null);
        } else {
            textField.setStyle(ERROR_STYLE);
        }
    }

    private void updateToggleGraphics(ToggleButton toggle) {
        if (toggle == null) {
            return;
        }

        if (toggle.isSelected()) {
            toggle.setGraphic(ToggleGraphics.getGraphic(
                    TOGGLE_WIDTH, TOGGLE_HEIGHT, true, TOGGLE_TEXT_YES, TOGGLE_STYLE_YES));
        } else {
            toggle.setGraphic(ToggleGraphics.getGraphic(
                    TOGGLE_WIDTH, TOGGLE_HEIGHT, false, TOGGLE_TEXT_NO, TOGGLE_STYLE_NO));
        }
    }
}
