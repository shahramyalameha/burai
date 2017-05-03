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
import burai.com.consts.ConstantStyles;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class QEFXModelerEditorController extends QEFXAppController {

    private static final double CTRL_GRAPHIC_SIZE = 20.0;
    private static final String CTRL_GRAPHIC_CLASS = "piclight-button";

    private static final double BUILD_GRAPHIC_SIZE = 20.0;
    private static final String BUILD_GRAPHIC_CLASS = "piclight-button";

    private static final String ERROR_STYLE = ConstantStyles.ERROR_COLOR;

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
        this.setupScaleField(this.scaleField1);
        this.setupScaleField(this.scaleField2);
        this.setupScaleField(this.scaleField3);

        this.setupSlabButton();
        this.setupMillerField(this.millerField1, 0);
        this.setupMillerField(this.millerField2, 0);
        this.setupMillerField(this.millerField3, 1);
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

        this.superButton.setOnAction(event -> {
            if (this.modeler == null) {
                return;
            }

            int n1 = this.getScaleValue(this.scaleField1);
            int n2 = this.getScaleValue(this.scaleField2);
            int n3 = this.getScaleValue(this.scaleField3);
            if ((n1 * n2 * n3) > 1) {
                this.modeler.buildSuperCell(n1, n2, n3);
            }
        });
    }

    private void setupScaleField(TextField textField) {
        if (textField == null) {
            return;
        }

        textField.setText("1");
        textField.setStyle("");

        textField.textProperty().addListener(o -> {
            int value = this.getScaleValue(textField);
            if (value > 0) {
                textField.setStyle("");
            } else {
                textField.setStyle(ERROR_STYLE);
            }
        });
    }

    private int getScaleValue(TextField textField) {
        if (textField == null) {
            return 0;
        }

        String text = textField.getText();
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int value = 0;

        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }

        return value;
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

        this.slabButton.setOnAction(event -> {
            if (this.modeler == null) {
                return;
            }

            int m1 = this.getMillerValue(this.millerField1);
            int m2 = this.getMillerValue(this.millerField2);
            int m3 = this.getMillerValue(this.millerField3);
            if (m1 != 0 || m2 != 0 || m3 != 0) {
                this.modeler.buildSlabModel(m1, m2, m3);
            }
        });
    }

    private void setupMillerField(TextField textField, int initValue) {
        if (textField == null) {
            return;
        }

        textField.setText(Integer.toString(initValue));
        textField.setStyle("");

        textField.textProperty().addListener(o -> {
            Integer value = this.getMillerValue(textField);
            if (value != null) {
                textField.setStyle("");
            } else {
                textField.setStyle(ERROR_STYLE);
                return;
            }

            Integer M1 = this.getMillerValue(this.millerField1);
            if (M1 == null) {
                return;
            }

            Integer M2 = this.getMillerValue(this.millerField2);
            if (M2 == null) {
                return;
            }

            Integer M3 = this.getMillerValue(this.millerField3);
            if (M3 == null) {
                return;
            }

            int m1 = M1.intValue();
            int m2 = M2.intValue();
            int m3 = M3.intValue();

            if (m1 != 0 || m2 != 0 || m3 != 0) {
                if (this.millerField1 != null) {
                    this.millerField1.setStyle("");
                }
                if (this.millerField2 != null) {
                    this.millerField2.setStyle("");
                }
                if (this.millerField3 != null) {
                    this.millerField3.setStyle("");
                }

            } else {
                if (this.millerField1 != null) {
                    this.millerField1.setStyle(ERROR_STYLE);
                }
                if (this.millerField2 != null) {
                    this.millerField2.setStyle(ERROR_STYLE);
                }
                if (this.millerField3 != null) {
                    this.millerField3.setStyle(ERROR_STYLE);
                }
            }
        });
    }

    private Integer getMillerValue(TextField textField) {
        if (textField == null) {
            return null;
        }

        String text = textField.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }

        int value = 0;

        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }

        return value;
    }
}
