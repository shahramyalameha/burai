/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.modeler.slabmodel;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import burai.app.QEFXAppController;
import burai.app.QEFXMain;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.modeler.slabmodel.SlabModel;
import burai.app.project.viewer.modeler.slabmodel.SlabModeler;
import burai.atoms.model.Cell;
import burai.atoms.vlight.AtomsVLight;
import burai.com.consts.ConstantStyles;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;
import burai.com.keys.KeyNames;

public class QEFXSlabEditorController extends QEFXAppController {

    private static final double CTRL_GRAPHIC_SIZE = 20.0;
    private static final String CTRL_GRAPHIC_CLASS = "piclight-button";

    private static final double BUILD_GRAPHIC_SIZE = 20.0;
    private static final String BUILD_GRAPHIC_CLASS = "piclight-button";

    private static final String ERROR_STYLE = ConstantStyles.ERROR_COLOR;

    private static final double ATOMS_TILE_SIZE = 184.0;
    private static final double ATOMS_TILE_SCLAE = 1.62;
    private static final String ATOMS_TILE_STYLE = "-fx-background-color: -fx-focus-color";
    private static final double ATOMS_LABEL_INSET = 12.0;
    private static final String ATOMS_LABEL_STYLE = "-fx-font: italic 1.166667em \"Arial Black\"";

    private QEFXProjectController projectController;

    private SlabModeler modeler;

    @FXML
    private Button screenButton;

    @FXML
    private Button initButton;

    @FXML
    private Button centerButton;

    @FXML
    private Label centerLabel;

    @FXML
    private Button superButton;

    @FXML
    private TextField scaleField1;

    @FXML
    private TextField scaleField2;

    @FXML
    private Slider slabSlider;

    @FXML
    private Slider vacuumSlider;

    @FXML
    private TilePane kindPane;

    @FXML
    private ScrollPane kindScroll;

    public QEFXSlabEditorController(QEFXProjectController projectController, SlabModeler modeler) {
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
        this.setupInitButton();
        this.setupCenterButton();
        this.setupCenterLabel();

        this.setupSuperButton();
        this.setupScaleField(this.scaleField1);
        this.setupScaleField(this.scaleField2);

        this.setupSlabSlider();
        this.setupVacuumSlider();
        this.setupKindPane();
        this.setupKindScroll();
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

    private void setupCenterButton() {
        if (this.centerButton == null) {
            return;
        }

        this.centerButton.setText("");
        this.centerButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CENTER, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.centerButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.center();
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

    private void setupSuperButton() {
        if (this.superButton == null) {
            return;
        }

        this.superButton.setDisable(true);
        this.superButton.getStyleClass().add(BUILD_GRAPHIC_CLASS);
        this.superButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.GEAR, BUILD_GRAPHIC_SIZE, null, BUILD_GRAPHIC_CLASS));

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
            boolean status = this.modeler.scaleSlabArea(n1, n2);

            if (!status) {
                this.showErrorDialog();
            }

            if (this.scaleField1 != null) {
                this.scaleField1.setText("");
            }
            if (this.scaleField2 != null) {
                this.scaleField2.setText("");
            }
        });
    }

    private void showErrorDialog() {
        Alert alert = new Alert(AlertType.ERROR);
        QEFXMain.initializeDialogOwner(alert);
        alert.setHeaderText("Error has occurred in modering.");
        alert.setContentText("Atoms are too much.");
        alert.showAndWait();
    }

    private boolean isAvailSuper() {
        int n1 = this.getScaleValue(this.scaleField1);
        if (n1 < 1) {
            return false;
        }

        int n2 = this.getScaleValue(this.scaleField2);
        if (n2 < 1) {
            return false;
        }

        return (n1 * n2) > 1;
    }

    private void setupScaleField(TextField textField) {
        if (textField == null) {
            return;
        }

        textField.setText("");
        textField.setStyle("");

        textField.textProperty().addListener(o -> {
            int value = this.getScaleValue(textField);
            if (value > 0) {
                textField.setStyle("");
            } else {
                textField.setStyle(ERROR_STYLE);
            }

            if (this.superButton != null) {
                this.superButton.setDisable(!this.isAvailSuper());
            }
        });

        textField.setOnAction(event -> {
            if (this.superButton != null && !(this.superButton.isDisable())) {
                EventHandler<ActionEvent> handler = this.superButton.getOnAction();
                if (handler != null) {
                    handler.handle(event);
                }
            }
        });
    }

    private int getScaleValue(TextField textField) {
        if (textField == null) {
            return 0;
        }

        String text = textField.getText();
        text = text == null ? null : text.trim();
        if (text == null || text.isEmpty()) {
            return 1;
        }

        int value = 0;

        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }

        return value;
    }

    private void setupSlabSlider() {
        if (this.slabSlider == null) {
            return;
        }

        this.slabSlider.valueProperty().addListener(o -> {
            if (this.modeler == null) {
                return;
            }

            double rate = this.slabSlider.getValue();

            boolean status = false;
            if (rate > 0.0) {
                status = this.modeler.changeSlabWidth(rate);
            }

            if (!status) {
                this.showErrorDialog();
            }

            if (this.scaleField1 != null) {
                this.scaleField1.setText("");
            }
            if (this.scaleField2 != null) {
                this.scaleField2.setText("");
            }
        });
    }

    private void setupVacuumSlider() {
        if (this.vacuumSlider == null) {
            return;
        }

        this.vacuumSlider.valueProperty().addListener(o -> {
            if (this.modeler == null) {
                return;
            }

            double vacuum = this.vacuumSlider.getValue();

            boolean status = false;
            if (vacuum > 0.0) {
                status = this.modeler.changeVacuumWidth(vacuum);
            }

            if (!status) {
                //this.showErrorDialog();
            }

            if (this.scaleField1 != null) {
                this.scaleField1.setText("");
            }
            if (this.scaleField2 != null) {
                this.scaleField2.setText("");
            }
        });
    }

    private void setupKindPane() {
        if (this.kindPane == null) {
            return;
        }

        this.kindPane.setOnMouseClicked(event -> {
            if (this.kindScroll != null) {
                this.kindScroll.requestFocus();
            }
        });
    }

    private void setupKindScroll() {
        if (this.kindScroll == null) {
            return;
        }

        this.kindScroll.setFocusTraversable(true);
    }

    public void setSlabModels(SlabModel[] slabModels) {
        if (slabModels == null || slabModels.length < 1) {
            return;
        }

        if (this.kindPane == null) {
            return;
        }

        for (int i = 0; i < slabModels.length; i++) {
            SlabModel slabModel = slabModels[i];
            if (slabModel == null) {
                continue;
            }

            Cell cell = Cell.getEmptyCell();
            if (cell == null) {
                return;
            }

            slabModel.updateCell(cell);

            AtomsVLight atomsVLight = new AtomsVLight(cell, ATOMS_TILE_SIZE, true);
            atomsVLight.appendScale(ATOMS_TILE_SCLAE);

            StackPane pane = new StackPane(atomsVLight);
            pane.setOnMouseEntered(event -> {
                pane.setStyle(ATOMS_TILE_STYLE);
            });
            pane.setOnMouseExited(event -> {
                pane.setStyle("");
            });

            Label label = new Label("#" + Integer.toString(i + 1));
            label.setStyle(ATOMS_LABEL_STYLE);
            StackPane.setAlignment(label, Pos.TOP_LEFT);
            StackPane.setMargin(label, new Insets(ATOMS_LABEL_INSET));
            pane.getChildren().add(label);

            this.kindPane.getChildren().add(pane);
        }
    }

    public void cleanSlabModels() {
        if (this.kindPane != null) {
            this.kindPane.getChildren().clear();
        }
    }
}
